package com.cliptripbe.feature.place.domain.entity;

import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.feature.user.domain.type.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Place place;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String name;

    private String roadAddress;

    public static PlaceTranslation of(Place place, TranslationInfo translationInfo, Language language) {
        Objects.requireNonNull(place, "place must not be null");
        Objects.requireNonNull(language, "language must not be null");
        Objects.requireNonNull(translationInfo, "translationInfo must not be null");

        return PlaceTranslation.builder()
            .place(place)
            .name(translationInfo.translatedName())
            .roadAddress(translationInfo.translatedRoadAddress())
            .language(language)
            .build();
    }

    public void addPlace(Place place) {
        this.place = place;
    }
}
