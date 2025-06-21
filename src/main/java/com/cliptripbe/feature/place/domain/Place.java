package com.cliptripbe.feature.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class Place {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Embedded
    private Address address;

    @Column
    @Convert(converter = AccessibilityFeatureConverter.class)
    private List<AccessibilityFeature> accessibilityFeatures;

    @Builder
    public Place(String name, Address address,
        List<AccessibilityFeature> accessibilityFeatures) {
        this.name = name;
        this.address = address;
        this.accessibilityFeatures = accessibilityFeatures;
    }
}
