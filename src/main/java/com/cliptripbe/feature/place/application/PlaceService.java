package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    final PlaceFinder placeFinder;
    final PlaceRepository placeRepository;

    @Transactional
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        String placeName) {
        Place place = placeFinder.getPlaceByName(placeName);
        return PlaceAccessibilityInfoResponse.from(place);
    }
}
