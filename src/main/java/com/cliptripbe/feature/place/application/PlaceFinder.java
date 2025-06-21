package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceFinder {

    final PlaceRepository placeRepository;

    public Place getPlaceByName(String placeName) {
        return placeRepository.findByName(placeName).orElseThrow(
            () -> new EntityNotFoundException("없습니다.")
        );
    }
}
