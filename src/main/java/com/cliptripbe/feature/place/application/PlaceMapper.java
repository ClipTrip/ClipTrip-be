package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public Place mapPlaceCulture(String placeInfo) {
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
            .placeType(PlaceType.CULTURAL_FACILITY)
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

    public Place mapPlaceThng(String line) {
        String[] tokens = line.split(",");

        if (tokens.length < 17) {
            throw new IllegalArgumentException("CSV 라인의 필드 수가 부족합니다.");
        }

        String facilityName = tokens[1];             // FCLTY_NM
        String roadAddress = tokens[2];              // RDNMADR_NM
        String telNumber = tokens[16];               // TEL_NO
        Double longitude = parseDouble(tokens[13]);  // FCLTY_LO
        Double latitude = parseDouble(tokens[14]);   // FCLTY_LA

        Address address = Address.builder()
            .roadAddress(roadAddress)
            .longitude(longitude)
            .latitude(latitude)
            .build();

        // 접근성 정보가 없다면 비워두되, 기본값으로 빈 리스트 사용
        List<AccessibilityFeature> accessibilityFeatures = Collections.emptyList();

        return Place.builder()
            .name(facilityName)
            .phoneNumber(telNumber)
            .address(address)
            .accessibilityFeatures(accessibilityFeatures)
            .placeType(PlaceType.LUGGAGE_STORAGE)
            .build();
    }

    public Place mapPlaceAccmodation(String line) {

        String[] tokens = line.split(",");

        if (tokens.length < 15) {
            throw new IllegalArgumentException("CSV 필드 수 부족: " + tokens.length);
        }

        String name = tokens[1].trim();        // FCLTY_NM
        String roadAddress = tokens[2].trim();         // RDNMADR_NM
        Double longitude = parseDouble(tokens[13].trim()); // FCLTY_LO
        Double latitude = parseDouble(tokens[14].trim());  // FCLTY_LA

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