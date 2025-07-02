package com.cliptripbe.feature.place.application;


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
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.kakao.KakaoMapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceFinder placeFinder;
    private final KakaoMapService kakaoMapService;
    private final BookmarkRepository bookmarkRepository;

    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto
    ) {
        Place place = placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto);

        return PlaceAccessibilityInfoResponse.from(place);
    }

    public PlaceAccessibilityInfoResponse getPlaceInfo(PlaceInfoRequestDto placeInfoRequestDto,
        User user) {
        Place place = placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto);
        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        return PlaceAccessibilityInfoResponse.of(place, bookmarked);
    }

    public PlaceResponseDto getPlaceById(Long placeId, User user) {
        Place place = placeFinder.getPlaceById(placeId);
        boolean bookmarked = bookmarkRepository.isPlaceBookmarkedByUser(user.getId(),
            place.getId());
        return PlaceResponseDto.of(place, bookmarked);
    }

    public List<PlaceListResponseDto> getPlacesByCategory(PlaceSearchByCategoryRequestDto request) {
        List<PlaceDto> categoryPlaces = kakaoMapService.searchPlacesByCategory(request);
        return categoryPlaces.stream()
            .map((PlaceDto placeDto) ->
                PlaceListResponseDto.of(
                    placeDto,
                    PlaceType.findByCode(request.categoryCode()))
            )
            .toList();
    }

    public List<PlaceListResponseDto> getPlacesByKeyword(PlaceSearchByKeywordRequestDto request) {
        List<PlaceDto> keywordPlaces = kakaoMapService.searchPlaces(request)
            .subscribeOn(Schedulers.boundedElastic())
            .blockOptional()
            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));

        return keywordPlaces.stream()
            .map(PlaceListResponseDto::from)
            .toList();
    }
}
