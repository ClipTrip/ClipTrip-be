package com.cliptripbe.global.initialization.mapper;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.global.initialization.type.DefaultData;
import org.springframework.stereotype.Component;

@Component
public class AccommodationPlaceMapper implements PlaceCsvMapper {

    @Override
    public Place map(String line) {
        String[] tokens = line.split(",");

        if (tokens.length < 15) {
            throw new IllegalArgumentException("CSV 필드 수 부족: " + tokens.length);
        }

        String name = tokens[1].trim();        // FCLTY_NM
        String roadAddress = tokens[2].trim();         // RDNMADR_NM
        Double longitude = PlaceCsvMapper.parseDouble(tokens[13].trim()); // FCLTY_LO
        Double latitude = PlaceCsvMapper.parseDouble(tokens[14].trim());  // FCLTY_LA

        Address address = Address.builder()
            .roadAddress(roadAddress)
            .latitude(latitude)
            .longitude(longitude)
            .build();
        return Place
            .builder()
            .name(name)
            .address(address)
            .placeType(PlaceType.ACCOMMODATION)
            .build();
    }

    @Override
    public DefaultData getDefaultData() {
        return DefaultData.ACCOMMODATION_SEOUL;
    }
}
