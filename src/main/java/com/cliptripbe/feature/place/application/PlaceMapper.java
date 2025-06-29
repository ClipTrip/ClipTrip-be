package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.vo.Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    AccessibilityFeature[] featureEnums = {
        AccessibilityFeature.FREE_PARKING,
        AccessibilityFeature.PAID_PARKING,
        AccessibilityFeature.ENTRANCE_FOR_DISABLED,
        AccessibilityFeature.WHEELCHAIR_RENTAL,
        AccessibilityFeature.DISABLED_TOILET,
        AccessibilityFeature.EXCLUSIVE_PARKING,
        AccessibilityFeature.LARGE_PARKING,
        AccessibilityFeature.GUIDE_DOG_ALLOWED,
        AccessibilityFeature.BRAILLE_GUIDE,
        AccessibilityFeature.AUDIO_GUIDE_KR
    };

    public Place mapPlace(String placeInfo) {
        String[] tokens = placeInfo.split(",");

        // 기본 필드 추출
        String name = tokens[0];

        // Address 생성 (도로명주소, 우편번호, 위도, 경도)
        String roadAddress = tokens[15];
        double latitude = parseDouble(tokens[12]);
        double longitude = parseDouble(tokens[13]);

        Address address =
            Address.builder()
                .latitude(latitude)
                .longitude(longitude)
                .roadAddress(roadAddress)
                .build();
        // AccessibilityFeature 추출

        String[] flagFields = Arrays.copyOfRange(tokens, 24, 34); // 10개

        List<AccessibilityFeature> features = new ArrayList<>();
        for (int i = 0; i < flagFields.length; i++) {
            String cleaned = flagFields[i].replace("\"", "").trim();
            if ("Y".equalsIgnoreCase(cleaned)) {
                features.add(featureEnums[i]);
            }
        }

        // Place 생성
        Place place = Place.builder()
            .name(name)
            .address(address)
            .accessibilityFeatures(features)
            .build();

        return place;
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}