package com.cliptripbe.feature.place.domain.entity;

import com.cliptripbe.feature.place.domain.converter.AccessibilityFeatureConverter;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Place extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    @Embedded
    private Address address;

    @Column
    @Convert(converter = AccessibilityFeatureConverter.class)
    private List<AccessibilityFeature> accessibilityFeatures;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;

    @Column
    private String imageUrl;

    @Builder
    public Place(
        String name,
        String phoneNumber,
        Address address,
        List<AccessibilityFeature> accessibilityFeatures,
        PlaceType placeType,
        String imageUrl
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.accessibilityFeatures = accessibilityFeatures;
        this.placeType = placeType;
        this.imageUrl = imageUrl;
    }
}
