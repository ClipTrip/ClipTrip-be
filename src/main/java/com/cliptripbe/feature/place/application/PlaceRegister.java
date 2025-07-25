package com.cliptripbe.feature.place.application;

import static com.cliptripbe.global.util.StreamUtils.distinctByKey;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PlaceRegister {

    private final PlaceRepository placeRepository;

    public List<Place> createAllPlaces(List<Place> placeList) {
        return placeRepository.saveAll(placeList);
    }

    public Place createPlaceFromInfo(PlaceInfoRequestDto placeInfoRequestDto) {
        Place place = Place.builder()
            .name(placeInfoRequestDto.placeName())
            .phoneNumber(placeInfoRequestDto.phoneNumber())
            .address(placeInfoRequestDto.address())
            .placeType(placeInfoRequestDto.type())
            .build();
        placeRepository.save(place);
        return place;
    }
}
