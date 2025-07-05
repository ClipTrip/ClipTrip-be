package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceTranslationFinder {

    private final PlaceTranslationRepository placeTranslationRepository;

    public PlaceTranslation getByPlaceAndLanguage(Place place, Language language) {
        return placeTranslationRepository.findByPlaceAndLanguage(place, language).orElseThrow(
            () -> new CustomException(ErrorType.ENTITY_NOT_FOUND)
        );
    }
}
