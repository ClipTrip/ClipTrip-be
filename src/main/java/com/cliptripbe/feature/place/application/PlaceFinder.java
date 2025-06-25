package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceFinder {

    final PlaceRepository placeRepository;
    final PlaceRegister placeRegister;

    public Place getPlaceByName(String placeName) {
        Optional<Place> optionalPlace = placeRepository.findByName(placeName);
        return optionalPlace.orElseGet(() -> placeRegister.registerPlace(placeName));
    }
}
