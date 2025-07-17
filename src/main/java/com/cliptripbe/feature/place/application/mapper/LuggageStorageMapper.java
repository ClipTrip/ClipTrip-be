package com.cliptripbe.feature.place.application.mapper;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import java.util.Collections;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class LuggageStorageMapper implements PlaceCsvMapper {

    @Override
    public Place map(String line) {
        String[] tokens = line.split(",");

        if (tokens.length < 17) {
            throw new IllegalArgumentException("CSV 라인의 필드 수가 부족합니다.");
        }

        String facilityName = tokens[1];             // FCLTY_NM
        String roadAddress = tokens[2];              // RDNMADR_NM
        String telNumber = tokens[16];               // TEL_NO
        Double longitude = PlaceCsvMapper.parseDouble(tokens[13]);  // FCLTY_LO
        Double latitude = PlaceCsvMapper.parseDouble(tokens[14]);   // FCLTY_LA

        Address address = Address.builder()
            .roadAddress(roadAddress)
            .longitude(longitude)
            .latitude(latitude)
            .build();

        Set<AccessibilityFeature> accessibilityFeatures = Collections.emptySet();

        return Place.builder()
            .name(facilityName)
            .phoneNumber(telNumber)
            .address(address)
            .accessibilityFeatures(accessibilityFeatures)
            .placeType(PlaceType.LUGGAGE_STORAGE)
            .build();
    }

    @Override
    public DefaultData getDefaultData() {
        return DefaultData.STORAGE_SEOUL;
    }
}
