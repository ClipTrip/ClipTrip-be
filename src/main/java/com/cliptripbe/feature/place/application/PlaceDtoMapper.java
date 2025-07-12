package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PlaceDtoMapper {

    public PlaceListResponseDto toDto(Place place) {
        return PlaceListResponseDto.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .latitude(place.getAddress().latitude())
            .longitude(place.getAddress().longitude())
            .roadAddress(place.getAddress().roadAddress())
            .build();
    }

    public List<PlaceListResponseDto> toDtoList(List<Place> places) {
        return places.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
