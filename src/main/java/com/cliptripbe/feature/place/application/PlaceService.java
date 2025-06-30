package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.kakao.KakaoMapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceFinder placeFinder;
    private final PlaceRepository placeRepository;
    private final KakaoMapService kakaoMapService;

    @Transactional
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto
    ) {
        Place place = placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto);
        return PlaceAccessibilityInfoResponse.from(place);
    }

    public List<PlaceListResponseDto> getPlacesByCategory(String categoryCode, String x, String y,
        Integer radius) {

        return null;
    }
}
