package com.cliptripbe.feature.place.application;


import static com.cliptripbe.global.response.type.ErrorType.GOOGLE_PLACES_NO_RESPONSE;
import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByCategoryRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByKeywordRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.LuggageStorageRequestDto;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.google.service.GooglePlacesService;
import com.cliptripbe.infrastructure.kakao.service.KakaoMapService;
import com.cliptripbe.infrastructure.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private static final String S3_PLACE_PREFIX = "place/";

    private final BookmarkRepository bookmarkRepository;

    private final PlaceFinder placeFinder;
    private final PlaceRegister placeRegister;
    private final PlaceTranslationService placeTranslationService;
    private final PlaceTranslationFinder placeTranslationFinder;
    private final PlaceClassifier placeClassifier;
    private final PlaceDtoMapper placeDtoMapper;

    private final KakaoMapService kakaoMapService;
    private final S3Service s3Service;
    private final GooglePlacesService googlePlacesService;

    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto
    ) {
        Place place = findOrCreatePlaceByPlaceInfo(placeInfoRequestDto);
        return PlaceAccessibilityInfoResponse.from(place);
    }

    public PlaceAccessibilityInfoResponse getPlaceInfo(PlaceInfoRequestDto placeInfoRequestDto,
        User user) {
        Place place = findOrCreatePlaceByPlaceInfo(placeInfoRequestDto);
        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        return PlaceAccessibilityInfoResponse.of(place, bookmarked);
    }

    public PlaceResponseDto getPlaceById(Long placeId, User user) {
        Place place = placeFinder.getPlaceById(placeId);

        if (place.getImageUrl() == null || place.getImageUrl().isEmpty()) {
            String searchKeyWord = place.getName() + " " + place.getAddress().roadAddress();
            byte[] imageBytes = googlePlacesService.getPhotoByAddress(searchKeyWord)
                .blockOptional()
                .orElseThrow(() -> new CustomException(GOOGLE_PLACES_NO_RESPONSE));
            String imageUrl = s3Service.upload(S3_PLACE_PREFIX, imageBytes);
            place.addImageUrl(imageUrl);
        }

        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        if (user.getLanguage() == Language.KOREAN) {
            return PlaceResponseDto.of(place, bookmarked);
        }

        PlaceTranslation placeTranslation = placeTranslationFinder.getByPlaceAndLanguage(place,
            user.getLanguage());
        return PlaceResponseDto.of(place, bookmarked, placeTranslation);
    }


    public Place findOrCreatePlaceByPlaceInfo(PlaceInfoRequestDto placeInfoRequestDto) {
        Place place = placeFinder.getOptionPlaceByPlaceInfo(
            placeInfoRequestDto.placeName(),
            placeInfoRequestDto.roadAddress()
        ).orElseGet(() -> placeRegister.createPlaceFromInfo(placeInfoRequestDto));

        placeTranslationService.registerPlace(place);
        return place;
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlacesByCategory(PlaceSearchByCategoryRequestDto request) {
        List<PlaceDto> categoryPlaces = kakaoMapService.searchPlacesByCategory(request);
        return categoryPlaces.stream()
            .map((PlaceDto placeDto) ->
                PlaceListResponseDto.ofDto(
                    placeDto,
                    PlaceType.findByCode(request.categoryCode()))
            )
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlacesByKeyword(PlaceSearchByKeywordRequestDto request) {
        List<PlaceDto> keywordPlaces = kakaoMapService.searchPlaces(request)
            .subscribeOn(Schedulers.boundedElastic())
            .blockOptional()
            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));

        return keywordPlaces.stream()
            .map(PlaceListResponseDto::fromDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getLuggageStorage(
        LuggageStorageRequestDto luggageStorageRequestDto
    ) {
        List<Place> luggageStoragePlaces = placeFinder.getPlaceByType(PlaceType.LUGGAGE_STORAGE);

        List<Place> placesInRange = placeClassifier.getLuggagePlacesByRange(
            luggageStorageRequestDto,
            luggageStoragePlaces
        );
        return placeDtoMapper.toDtoList(placesInRange);
    }
}
