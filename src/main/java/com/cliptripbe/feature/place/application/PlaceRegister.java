package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import java.util.Collections;
import java.util.HashSet;
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

    public List<Place> registerAllPlaces(List<PlaceDto> placeDtoList) {
        List<String> addressList = placeDtoList.stream()
            .map(PlaceDto::roadAddress)
            .filter(Objects::nonNull)
            .toList();

        if (placeDtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Place> existsPlace = placeRepository.findExistingPlaceByAddress(addressList);
        Set<String> existingAddresses = existsPlace.stream()
            .map(place -> place.getAddress().roadAddress())
            .collect(Collectors.toSet());

        List<Place> toSavePlaces = placeDtoList.stream()
            .filter(dto -> !existingAddresses.contains(dto.roadAddress()))
            .distinct()
            .map(PlaceDto::toPlace)
            .toList();

        List<Place> savedPlaces = placeRepository.saveAll(toSavePlaces);
        savedPlaces.addAll(existsPlace);
        return savedPlaces;
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
