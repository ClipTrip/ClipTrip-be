package com.cliptripbe.feature.place.application;


import static com.cliptripbe.global.response.type.ErrorType.FAIL_CREATE_PLACE_ENTITY;
import static com.cliptripbe.global.util.StreamUtils.distinctByKey;

import com.cliptripbe.feature.bookmark.domain.service.BookmarkFinder;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.service.PlaceClassifier;
import com.cliptripbe.feature.place.domain.service.PlaceFinder;
import com.cliptripbe.feature.place.domain.service.PlaceRegister;
import com.cliptripbe.feature.place.domain.service.PlaceTranslationFinder;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.request.LuggageStorageRequest;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceResponse;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.feature.translate.application.PlaceTranslationService;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.port.kakao.PlaceSearchPort;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final BookmarkFinder bookmarkFinder;
    private final PlaceImageService placeImageService;

    private final PlaceRegister placeRegister;
    private final PlaceFinder placeFinder;
    private final PlaceClassifier placeClassifier;
    private final PlaceListResponseAssembler placeListResponseAssembler;
    private final PlaceRepository placeRepository;
    private final PlaceTranslationService placeTranslationService;
    private final PlaceTranslationFinder placeTranslationFinder;

    private final PlaceSearchPort placeSearchPort;

    private final EntityManager entityManager;


    public PlaceResponse getPlaceById(Long placeId, User user) {
        Place place = placeFinder.getPlaceById(placeId);

        if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
            placeImageService.savePlaceImage(place);
        }

        List<Long> bookmarkedIdList = bookmarkFinder.findBookmarkIdsByUserIdAndPlaceId(
            user.getId(), place.getId());

        // TODO : 여기도 번역 적용해주세요.
        if (user.getLanguage() == Language.KOREAN) {
            return PlaceResponse.of(place, bookmarkedIdList);
        }

        PlaceTranslation placeTranslation = placeTranslationFinder.getByPlaceAndLanguage(place,
            user.getLanguage());
        return PlaceResponse.ofTranslation(place, bookmarkedIdList, placeTranslation);
    }

    public PlaceResponse findOrCreateByKakaoPlaceId(PlaceInfoRequest request, User user) {
        Place place = findOrCreatePlaceByPlaceInfo(request, user.getLanguage());

        if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
            placeImageService.savePlaceImage(place);
        }
        List<Long> bookmarkedIdList = bookmarkFinder.findBookmarkIdsByUserIdAndPlaceId(
            user.getId(), place.getId());

        // TODO : 여기도 번역 적용해주세요.
        if (user.getLanguage() == Language.KOREAN) {
            return PlaceResponse.of(place, bookmarkedIdList);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Place findOrCreatePlaceByPlaceInfo(PlaceInfoRequest request, Language language) {
        // TODO : 이제 번역 장소 어떻게 저장할지 여기도 바꿔줘야함
        String kakaoPlaceId = request.kakaoPlaceId();
        try {
            if (kakaoPlaceId != null && !kakaoPlaceId.trim().isEmpty()) {
                Optional<Place> existingPlace = placeFinder.findByKakaoPlaceId(kakaoPlaceId);
                if (existingPlace.isPresent()) {
                    Place place = existingPlace.get();
                    if (language != Language.KOREAN) {
                        placeTranslationService.translateAndRegisterPlace(place, language);
                    }
                    return place;
                }
            }

            Place place = placeFinder.getOptionPlaceByPlaceInfo(request.placeName(),
                    request.roadAddress())
                .map(p -> {
                    if (kakaoPlaceId != null && !kakaoPlaceId.trim().isEmpty()) {
                        p.addKakaoPlaceId(kakaoPlaceId);
                        return placeRepository.saveAndFlush(p);
                    }
                    return p;
                })
                .orElseGet(() -> placeRegister.createPlaceFromInfo(request));
            if (language != Language.KOREAN) {
                placeTranslationService.translateAndRegisterPlace(place, language);
            }

            return place;

        } catch (DataIntegrityViolationException e) {
            entityManager.clear();

            try {
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            if (kakaoPlaceId == null || kakaoPlaceId.trim().isEmpty()) {
                throw new CustomException(FAIL_CREATE_PLACE_ENTITY);
            }

            Place place = placeRepository.findByKakaoPlaceId(kakaoPlaceId)
                .orElseThrow(() -> new CustomException(FAIL_CREATE_PLACE_ENTITY));
            if (language != Language.KOREAN) {
                placeTranslationService.translateAndRegisterPlace(place, language);
            }
            return place;
        }
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponse> getPlacesByCategory(
        PlaceSearchByCategoryRequest request,
        User user
    ) {
        List<PlaceDto> placeDtoList = placeSearchPort.searchPlacesByCategory(request);

        List<String> kakaoPlaceIdList = placeDtoList.stream()
            .map(PlaceDto::kakaoPlaceId)
            .filter(Objects::nonNull)
            .toList();

        Map<String, List<Long>> bookmarkIdsMap = bookmarkFinder.findBookmarkIdsByKakaoPlaceIds(
            user.getId(), kakaoPlaceIdList);

        Language userLanguage = user.getLanguage();

        if (userLanguage == Language.KOREAN) {
            return placeListResponseAssembler.createPlaceListResponseForKorean(placeDtoList,
                bookmarkIdsMap);
        }

        List<TranslatedPlaceAddress> translatedPlaces = placeTranslationService.getTranslatedPlaces(
            userLanguage,
            placeDtoList);
        Map<String, PlaceDto> placeDtoMap = placeDtoList.stream()
            .collect(Collectors.toMap(
                dto -> dto.placeName() + dto.roadAddress(),
                Function.identity()
            ));
        return placeListResponseAssembler.createPlaceListResponseForForeign(placeDtoMap,
            translatedPlaces,
            bookmarkIdsMap, userLanguage);
    }

    public List<PlaceListResponse> getPlacesByKeyword(
        PlaceSearchByKeywordRequest request,
        User user
    ) {
        List<PlaceDto> keywordPlaces = placeSearchPort.searchPlacesByKeyWord(request);

        List<String> kakaoPlaceIdList = keywordPlaces.stream()
            .map(PlaceDto::kakaoPlaceId)
            .filter(Objects::nonNull)
            .toList();

        Map<String, List<Long>> bookmarkIdsMap = bookmarkFinder.findBookmarkIdsByKakaoPlaceIds(
            user.getId(), kakaoPlaceIdList);

        Language userLanguage = user.getLanguage();

        if (userLanguage == Language.KOREAN) {
            return placeListResponseAssembler.createPlaceListResponseForKorean(keywordPlaces,
                bookmarkIdsMap);
        }

        List<TranslatedPlaceAddress> translatedPlaces = placeTranslationService.getTranslatedPlaces(
            userLanguage,
            keywordPlaces);
        Map<String, PlaceDto> placeDtoMap = keywordPlaces.stream()
            .collect(Collectors.toMap(
                dto -> dto.placeName() + dto.roadAddress(),
                Function.identity()
            ));
        return placeListResponseAssembler.createPlaceListResponseForForeign(placeDtoMap,
            translatedPlaces,
            bookmarkIdsMap, userLanguage);
    }

    @Transactional
    public List<Place> createPlaceAll(List<PlaceDto> placeDtoList) {
        if (placeDtoList == null || placeDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> kakaoPlaceIdList = placeDtoList.stream()
            .map(PlaceDto::kakaoPlaceId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<String> addressList = placeDtoList.stream()
            .map(PlaceDto::roadAddress)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<String> placeNameList = placeDtoList.stream()
            .map(PlaceDto::placeName)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<Place> existsPlaceList = placeFinder.findExistingPlaceByKakaoPlaceIdOrAddressAndName(
            kakaoPlaceIdList,
            addressList,
            placeNameList
        );

        Map<String, Place> placeByKakaoId = existsPlaceList.stream()
            .filter(p -> p.getKakaoPlaceId() != null)
            .collect(Collectors.toMap(Place::getKakaoPlaceId, Function.identity(),
                (p1, p2) -> p1)); // 중복 시 하나 선택

        Map<Pair<String, String>, Place> placeByAddressName = existsPlaceList.stream()
            .filter(p -> p.getAddress() != null
                && p.getAddress().roadAddress() != null
                && p.getName() != null)
            .collect(Collectors.toMap(
                p -> Pair.of(p.getAddress().roadAddress(), p.getName()),
                Function.identity(),
                (p1, p2) -> p1 // 중복 시 하나 선택
            ));

        List<Place> savePlaces = new ArrayList<>();
        List<Place> updatePlaces = new ArrayList<>();
        List<Place> finalPlaces = new ArrayList<>();

        for (PlaceDto dto : placeDtoList.stream().filter(distinctByKey(PlaceDto::kakaoPlaceId))
            .toList()) {
            if (placeByKakaoId.containsKey(dto.kakaoPlaceId())) {
                finalPlaces.add(placeByKakaoId.get(dto.kakaoPlaceId()));
                continue;
            }

            Pair<String, String> addressNameKey = Pair.of(dto.roadAddress(), dto.placeName());
            if (placeByAddressName.containsKey(addressNameKey)) {
                Place potentialPlace = placeByAddressName.get(addressNameKey);
                String kakaoPlaceId = potentialPlace.getKakaoPlaceId();
                if (kakaoPlaceId == null || kakaoPlaceId.isEmpty()) {
                    potentialPlace.addKakaoPlaceId(dto.kakaoPlaceId());
                    updatePlaces.add(potentialPlace);
                } else {
                    finalPlaces.add(potentialPlace);
                }
                continue;
            }

            savePlaces.add(dto.toPlace());
        }

        List<Place> placesToPersist = new ArrayList<>();
        placesToPersist.addAll(savePlaces);
        placesToPersist.addAll(updatePlaces);

        List<Place> savedOrUpdatedPlaces = placeRegister.createAllPlaces(placesToPersist);

        List<Place> result = new ArrayList<>(finalPlaces);
        result.addAll(savedOrUpdatedPlaces);
        return result;
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponse> getLuggageStorage(
        LuggageStorageRequest luggageStorageRequest,
        User user
    ) {
        List<Place> luggageStoragePlaces = placeFinder.getPlaceByType(PlaceType.LUGGAGE_STORAGE);

        List<Long> placeIdList = luggageStoragePlaces.stream()
            .map(Place::getId)
            .toList();

        Map<Long, List<Long>> bookmarkIdsMap = bookmarkFinder.findBookmarkIdsByPlaceIds(
            user.getId(), placeIdList);

        List<Place> placesInRange = placeClassifier.getLuggagePlacesByRange(
            luggageStorageRequest, luggageStoragePlaces);

        // TODO : 응답할 때 번역 부분 적용해야 함
        return placesInRange.stream()
            .map(place -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(place.getId(), List.of());
                return PlaceListResponse.ofEntity(place, null, user.getLanguage(), bookmarkIds);
            })
            .toList();
    }

    @Transactional
    public List<Place> findOrCreatePlacesByPlaceInfos(List<PlaceInfoRequest> request) {
        if (request == null || request.isEmpty()) {
            return Collections.emptyList();
        }

        List<PlaceDto> placeDtoList = request.stream()
            .map(PlaceDto::fromDto)
            .toList();

        List<Place> allPlaces = createPlaceAll(placeDtoList);

        Map<String, Place> placeByKakaoId = allPlaces.stream()
            .filter(p -> p.getKakaoPlaceId() != null)
            .collect(Collectors.toMap(Place::getKakaoPlaceId, Function.identity()));

        Map<String, Place> placeByAddressName = allPlaces.stream()
            .filter(p -> p.getAddress() != null
                && p.getAddress().roadAddress() != null
                && p.getName() != null)
            .collect(Collectors.toMap(
                p -> p.getName() + "|" + p.getAddress().roadAddress(),
                Function.identity()
            ));

        return request.stream()
            .map(placeRequest -> {
                if (placeRequest.kakaoPlaceId() != null && !placeRequest.kakaoPlaceId().trim()
                    .isEmpty()) {
                    Place place = placeByKakaoId.get(placeRequest.kakaoPlaceId());
                    if (place != null) {
                        return place;
                    }
                }
                // key : placeName / value : roadAddress
                String key = placeRequest.placeName() + "|" + placeRequest.roadAddress();
                return placeByAddressName.get(key);
            })
            .filter(Objects::nonNull)
            .toList();
    }

    @Transactional
    public Map<Long, TranslationInfoDto> getTranslationsForPlaces(
        List<Place> places,
        Language language
    ) {
        List<Long> placeIds = places.stream()
            .map(Place::getId)
            .toList();

        List<PlaceTranslation> translations = placeTranslationService.findByPlaceIdInAndLanguage(
            placeIds, language);
        return translations.stream()
            .collect(Collectors.toMap(
                translation -> translation.getPlace().getId(),
                TranslationInfoDto::fromEntity
            ));
    }
}
