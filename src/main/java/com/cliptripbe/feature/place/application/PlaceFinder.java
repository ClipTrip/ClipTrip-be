package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceFinder {

    private final PlaceRepository placeRepository;

    public Place getPlaceByPlaceInfo(PlaceInfoRequestDto placeInfoRequestDto) {
        Optional<Place> place = placeRepository.findPlaceByPlaceInfo(
            placeInfoRequestDto.placeName(),
            placeInfoRequestDto.roadAddress()
        );
        if (!place.isPresent()) {
            throw new CustomException(ErrorType.ENTITY_NOT_FOUND);
        }
        return place.get();
    }

    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(
            () -> new CustomException(ErrorType.ENTITY_NOT_FOUND)
        );
    }
}
