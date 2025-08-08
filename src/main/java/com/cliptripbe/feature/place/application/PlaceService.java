package com.cliptripbe.feature.place.application;


import static com.cliptripbe.global.util.StreamUtils.distinctByKey;

import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.service.PlaceClassifier;
import com.cliptripbe.feature.place.domain.service.PlaceFinder;
import com.cliptripbe.feature.place.domain.service.PlaceRegister;
import com.cliptripbe.feature.place.domain.service.PlaceTranslationFinder;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.LuggageStorageRequestDto;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.feature.place.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceResponse;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.infrastructure.port.google.PlaceImageProviderPort;
import com.cliptripbe.infrastructure.port.kakao.PlaceSearchPort;
import com.cliptripbe.infrastructure.port.s3.FileStoragePort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PlaceService {

    private static final String S3_PLACE_PREFIX = "place/";

    private final BookmarkRepository bookmarkRepository;

    private final PlaceRegister placeRegister;
    private final PlaceFinder placeFinder;
    private final PlaceRepository placeRepository;

    private final PlaceTranslationService placeTranslationService;
    private final PlaceTranslationFinder placeTranslationFinder;
    private final PlaceClassifier placeClassifier;

    private final PlaceSearchPort placeSearchPort;
    private final FileStoragePort fileStoragePort;
    private final PlaceImageProviderPort placeImageProviderPort;

    @Transactional(readOnly = true)
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequest placeInfoRequest
    ) {
        Place place = findOrCreatePlaceByPlaceInfo(placeInfoRequest);
        return PlaceAccessibilityInfoResponse.from(place);
    }

    @Transactional(readOnly = true)
    public PlaceAccessibilityInfoResponse getPlaceInfo(PlaceInfoRequest request, User user) {
        Place place = findOrCreatePlaceByPlaceInfo(request);
        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        return PlaceAccessibilityInfoResponse.of(place, bookmarked);
    }

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(Long placeId, User user) {
        Place place = placeFinder.getPlaceById(placeId);

        if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
            String searchKeyWord = place.getName() + " " + place.getAddress().roadAddress();
            byte[] imageBytes = placeImageProviderPort.getPhotoByAddress(searchKeyWord);
            String imageUrl = fileStoragePort.upload(S3_PLACE_PREFIX, imageBytes);
            place.addImageUrl(imageUrl);
        }

        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        if (user.getLanguage() == Language.KOREAN) {
            return PlaceResponse.of(place, bookmarked);
        }

        PlaceTranslation placeTranslation = placeTranslationFinder.getByPlaceAndLanguage(place,
            user.getLanguage());
        return PlaceResponse.of(place, bookmarked, placeTranslation);
    }

    @Transactional
    public Place findOrCreatePlaceByPlaceInfo(PlaceInfoRequest request) {
        String kakaoPlaceId = request.kakaoPlaceId();
        String placeName = request.placeName();
        String address = request.roadAddress();

        Place place = placeFinder.findByKakaoPlaceId(kakaoPlaceId)
            .or(() -> placeFinder.getOptionPlaceByPlaceInfo(placeName, address)
                .map(p -> {
                    p.addKakaoPlaceId(kakaoPlaceId);
                    return placeRepository.save(p);
                })
            )
            .orElseGet(() -> placeRegister.createPlaceFromInfo(request));
        placeTranslationService.registerPlace(place);
        return place;
    }

    public List<PlaceListResponse> getPlacesByCategory(PlaceSearchByCategoryRequest request) {
        List<PlaceDto> categoryPlaces = placeSearchPort.searchPlacesByCategory(request);
        return categoryPlaces.stream()
            .map((PlaceDto placeDto) ->
                PlaceListResponse.ofDto(
                    placeDto,
                    PlaceType.findByCode(request.categoryCode()))
            )
            .toList();
    }

    public List<PlaceListResponse> getPlacesByKeyword(PlaceSearchByKeywordRequest request) {
        List<PlaceDto> keywordPlaces = placeSearchPort.searchPlacesByKeyWord(request);

        return keywordPlaces.stream()
            .map(PlaceListResponse::fromDto)
            .toList();
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
        LuggageStorageRequestDto luggageStorageRequestDto
    ) {
        List<Place> luggageStoragePlaces = placeFinder.getPlaceByType(PlaceType.LUGGAGE_STORAGE);

        List<Place> placesInRange = placeClassifier.getLuggagePlacesByRange(
            luggageStorageRequestDto,
            luggageStoragePlaces
        );
        return PlaceListResponse.fromList(placesInRange);
    }

    @Transactional(readOnly = true)
    public List<Place> findOrCreatePlacesByPlaceInfos(List<PlaceInfoRequest> placeInfoRequests) {
        return placeFinder.findExistingPlaceByAddress(placeInfoRequests);
    }
}
