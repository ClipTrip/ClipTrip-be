package com.cliptripbe.global.initialization.mapper;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


    public Place mapPlaceFour(String line, PlaceType placeType) {
        String[] tokens = line.split(",");
        String name = tokens[0].trim();
        String roadAddress = tokens[1].trim();
        Double longitude = parseDouble(tokens[2].trim());
        Double latitude = parseDouble(tokens[3].trim());

        Address address = Address.builder()
            .roadAddress(roadAddress)
            .latitude(latitude)
            .longitude(longitude)
            .build();
        return Place
            .builder()
            .name(name)
            .address(address)
            .placeType(placeType)
            .build();
    }
}