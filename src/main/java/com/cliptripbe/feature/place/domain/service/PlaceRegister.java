package com.cliptripbe.feature.place.domain.service;


import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceRegister {

    private final PlaceRepository placeRepository;

    public List<Place> createAllPlaces(List<Place> placeList) {
        return placeRepository.saveAll(placeList);
    }

    public Place createPlaceFromInfo(PlaceInfoRequest placeInfoRequest) {
        Place place = Place.builder()
            .name(placeInfoRequest.placeName())
            .phoneNumber(placeInfoRequest.phone())
            .address(placeInfoRequest.getAddress())
            .placeType(placeInfoRequest.type())
            .kakaoPlaceId(placeInfoRequest.kakaoPlaceId())
            .build();

        placeRepository.saveAndFlush(place);
        return place;
    }


}
