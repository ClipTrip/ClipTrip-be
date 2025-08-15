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
import java.util.HashSet;
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

    @Convert(converter = AccessibilityFeatureConverter.class)
    private Set<AccessibilityFeature> accessibilityFeatures;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;

    @Column
    private String imageKey;

    @Column(unique = true)
    private String kakaoPlaceId;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceTranslation> placeTranslations = new HashSet<>();

    @Builder
    public Place(
        String name,
        String phoneNumber,
        Address address,
        Set<AccessibilityFeature> accessibilityFeatures,
        PlaceType placeType,
        String imageKey,
        String kakaoPlaceId
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.accessibilityFeatures = accessibilityFeatures != null
            ? new HashSet<>(accessibilityFeatures)
            : new HashSet<>();
        this.placeType = placeType;
        this.imageKey = imageKey;
        this.kakaoPlaceId = kakaoPlaceId;
    }

    public void addImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public PlaceTranslation getTranslationByLanguage(Language language) {
        return placeTranslations.stream()
            .filter(pt -> pt.getLanguage() == language)
            .findFirst()
            .orElse(null);
    }

    public void addTranslation(PlaceTranslation translation) {
        placeTranslations.add(translation);
        translation.addPlace(this);
    }

    public void addAccessibilityFeatures(Set<AccessibilityFeature> accessibilityFeatures) {
        if (accessibilityFeatures != null) {
            if (this.accessibilityFeatures == null) {
                this.accessibilityFeatures = new HashSet<>();
            }
            this.accessibilityFeatures.addAll(accessibilityFeatures);
        }
    }

    public void addKakaoPlaceId(String kakaoPlaceId) {
        this.kakaoPlaceId = kakaoPlaceId;
    }

    public boolean hasImageKey() {
        return imageKey != null && !imageKey.isBlank();
    }

    public String getTranslationKey() {
        return name + address.roadAddress();
    }
}
