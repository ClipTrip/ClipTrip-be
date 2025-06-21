package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.domain.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.file.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    final PlaceFinder placeFinder;
    final FileService fileService;
    final PlaceMapper placeMapper;
    final PlaceRepository placeRepository;

    @Transactional
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        String placeName) {
        Place place;
        try {
            place = placeFinder.getPlaceByName(placeName);
        } catch (EntityNotFoundException e) {
            place = registerPlace(placeName);
        }

        return PlaceAccessibilityInfoResponse.from(place);
    }

    private Place registerPlace(String placeName) {
        String placeInfo = fileService.findPlaceInfo(placeName);
        Place place = placeMapper.mapPlace(placeInfo);
        placeRepository.save(place);
        return place;
    }
}
