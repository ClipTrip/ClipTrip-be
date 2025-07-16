package com.cliptripbe.feature.place.domain.entity;

import com.cliptripbe.feature.place.domain.converter.AccessibilityFeatureConverter;
import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Set;
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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceTranslation> placeTranslations;

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

    public void addImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PlaceTranslation getTranslationByLanguage(Language language) {
        return placeTranslations.stream()
            .filter(pt -> pt.getLanguage() == language)
            .findFirst()
            .orElse(null);
    }
}
