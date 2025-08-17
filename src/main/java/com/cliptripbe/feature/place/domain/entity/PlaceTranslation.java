package com.cliptripbe.feature.place.domain.entity;

import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.type.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "place_translation", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"place_id", "language"})
})
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

    public static PlaceTranslation of(Place place, TranslationInfoDto translationInfoDto, Language language) {
        Objects.requireNonNull(place, "place must not be null");
        Objects.requireNonNull(language, "language must not be null");
        Objects.requireNonNull(translationInfoDto, "translationInfo must not be null");

        return PlaceTranslation.builder()
            .place(place)
            .name(translationInfoDto.translatedName())
            .roadAddress(translationInfoDto.translatedRoadAddress())
            .language(language)
            .build();
    }

    public void addPlace(Place place) {
        this.place = place;
    }
}
