package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceFinder {

    private final PlaceRepository placeRepository;

    public Optional<Place> getOptionPlaceByPlaceInfo(String placeName, String roadAddress) {
        return placeRepository.findPlaceByPlaceInfo(
            placeName,
            roadAddress
        );
    }

    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
            .orElseThrow(
                () -> new CustomException(ErrorType.ENTITY_NOT_FOUND)
            );
    }

    public List<Place> getPlaceByType(PlaceType placeType) {
        return placeRepository.findByPlaceType(placeType);
    }

    public List<Place> findExistingPlaceByAddressAndName(
        List<String> addressList,
        List<String> placeNameList
    ) {
        return placeRepository.findExistingPlaceByAddressAndName(addressList, placeNameList);
    }

    public Optional<Place> findByNameAndRoadAddress(String name, String roadAddress) {
        return placeRepository.findByNameAndAddressRoadAddress(
            name,
            roadAddress);
    }
}
