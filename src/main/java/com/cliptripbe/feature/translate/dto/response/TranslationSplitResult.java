package com.cliptripbe.feature.translate.dto.response;

import com.cliptripbe.feature.place.dto.PlaceDto;
import java.util.ArrayList;
import java.util.List;

public record TranslationSplitResult(
    List<TranslatedPlaceAddress> translatedPlaces,
    List<PlaceDto> untranslatedPlaces
) {

    public List<TranslatedPlaceAddress> mergeWith(List<TranslatedPlaceAddress> newTranslations) {
        List<TranslatedPlaceAddress> allTranslatedPlaces = new ArrayList<>(this.translatedPlaces);
        allTranslatedPlaces.addAll(newTranslations);
        return allTranslatedPlaces;
    }

}