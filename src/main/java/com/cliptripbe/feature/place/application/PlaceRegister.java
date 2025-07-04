package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceRegister {

    private final PlaceRepository placeRepository;

    public List<Place> registerAllPlaces(List<PlaceDto> placeDtoList) {
        List<Place> entities = placeDtoList.stream()
            .map(PlaceDto::toPlace)
            .toList();

        return placeRepository.saveAll(entities);
    }

    public Place createPlaceFromInfo(PlaceInfoRequestDto placeInfoRequestDto) {
        Place place = Place.builder()
            .name(placeInfoRequestDto.placeName())
            .phoneNumber(placeInfoRequestDto.phoneNumber())
            .address(placeInfoRequestDto.address())
            .placeType(PlaceType.ETC)
            .build();
        placeRepository.save(place);
        return place;
    }
}
