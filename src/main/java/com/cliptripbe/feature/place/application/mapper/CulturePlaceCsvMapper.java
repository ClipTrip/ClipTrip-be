package com.cliptripbe.feature.place.application.mapper;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CulturePlaceCsvMapper implements PlaceCsvMapper {

    @Override
    public Place map(String line) {
        String[] tokens = line.split(",");
        String name = tokens[0];
        String roadAddress;
        if (tokens[15].isBlank()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 5; i <= 11; i++) {
                if (!tokens[i].isBlank()) {
                    sb.append(tokens[i].trim());
                    if (i < 11) {
                        sb.append(" ");
                    }
                }
            }
            roadAddress = sb.toString().trim();
        } else {
            roadAddress = tokens[15].trim();
        }

        double latitude = PlaceCsvMapper.parseDouble(tokens[12]);
        double longitude = PlaceCsvMapper.parseDouble(tokens[13]);

        Address address = Address.builder()
            .latitude(latitude)
            .longitude(longitude)
            .roadAddress(roadAddress)
            .build();

        Set<AccessibilityFeature> features = new HashSet<>();
        String[] flagFields = Arrays.copyOfRange(tokens, 24, 34);
        for (int i = 0; i < flagFields.length; i++) {
            String cleaned = flagFields[i].replace("\"", "").trim();
            if ("Y".equalsIgnoreCase(cleaned)) {
                features.add(FEATURE_ENUMS[i]);
            }
        }
        return Place.builder()
            .name(name)
            .address(address)
            .accessibilityFeatures(features)
            .placeType(PlaceType.CULTURAL_FACILITY)
            .build();
    }

    @Override
    public DefaultData getDefaultData() {
        return DefaultData.BF_CULTURE_TOURISM;
    }
}
