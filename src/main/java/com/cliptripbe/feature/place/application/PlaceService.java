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
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.infrastructure.google.service.GooglePlacesService;
import com.cliptripbe.infrastructure.kakao.service.KakaoMapService;
import com.cliptripbe.infrastructure.s3.S3Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private static final String S3_PLACE_PREFIX = "place/";

    private final BookmarkRepository bookmarkRepository;

    private final PlaceRegister placeRegister;
    private final PlaceFinder placeFinder;

    private final PlaceTranslationService placeTranslationService;
    private final PlaceTranslationFinder placeTranslationFinder;
    private final PlaceClassifier placeClassifier;

    private final KakaoMapService kakaoMapService;
    private final S3Service s3Service;
    private final GooglePlacesService googlePlacesService;

    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequest placeInfoRequest
    ) {
        Place place = findOrCreatePlaceByPlaceInfo(placeInfoRequest);
        return PlaceAccessibilityInfoResponse.from(place);
    }

    public PlaceAccessibilityInfoResponse getPlaceInfo(PlaceInfoRequest placeInfoRequest,
        User user) {
        Place place = findOrCreatePlaceByPlaceInfo(placeInfoRequest);
        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        return PlaceAccessibilityInfoResponse.of(place, bookmarked);
    }

    public PlaceResponse getPlaceById(Long placeId, User user) {
        Place place = placeFinder.getPlaceById(placeId);

        if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
            String searchKeyWord = place.getName() + " " + place.getAddress().roadAddress();
            byte[] imageBytes = googlePlacesService.getPhotoByAddress(searchKeyWord);
            String imageUrl = s3Service.upload(S3_PLACE_PREFIX, imageBytes);
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


    public Place findOrCreatePlaceByPlaceInfo(PlaceInfoRequest placeInfoRequest) {
        Place place = placeFinder.getOptionPlaceByPlaceInfo(
            placeInfoRequest.placeName(),
            placeInfoRequest.roadAddress()
        ).orElseGet(() -> placeRegister.createPlaceFromInfo(placeInfoRequest));

        placeTranslationService.registerPlace(place);
        return place;
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponse> getPlacesByCategory(PlaceSearchByCategoryRequest request) {
        List<PlaceDto> categoryPlaces = kakaoMapService.searchPlacesByCategory(request);
        return categoryPlaces.stream()
            .map((PlaceDto placeDto) ->
                PlaceListResponse.ofDto(
                    placeDto,
                    PlaceType.findByCode(request.categoryCode()))
            )
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponse> getPlacesByKeyword(PlaceSearchByKeywordRequest request) {
        List<PlaceDto> keywordPlaces = kakaoMapService.searchPlaces(request);

        return keywordPlaces.stream()
            .map(PlaceListResponse::fromDto)
            .toList();
    }

    public List<Place> createPlaceAll(List<PlaceDto> placeDtoList) {
        if (placeDtoList.isEmpty()) {
            return Collections.emptyList();
        }

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

        List<Place> existsPlaceList = placeFinder.findExistingPlaceByAddressAndName(
            addressList,
            placeNameList
        );

        Set<Pair<String, String>> existingPairs = existsPlaceList.stream()
            .map(place -> Pair.of(place.getAddress().roadAddress(), place.getName()))
            .collect(Collectors.toSet());

        List<Place> placeList = placeDtoList.stream()
            .filter(dto -> !existingPairs.contains(Pair.of(dto.roadAddress(), dto.placeName())))
            .filter(distinctByKey(dto -> Pair.of(dto.roadAddress(), dto.placeName())))
            .map(PlaceDto::toPlace)
            .toList();

        List<Place> savedPlaceList = placeRegister.createAllPlaces(placeList);
        savedPlaceList.addAll(existsPlaceList);
        return savedPlaceList;
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

    public List<Place> findOrCreatePlacesByPlaceInfos(
        List<PlaceInfoRequest> placeInfoRequests
    ) {
        return placeFinder.findExistingPlaceByAddress(placeInfoRequests);
    }
}
