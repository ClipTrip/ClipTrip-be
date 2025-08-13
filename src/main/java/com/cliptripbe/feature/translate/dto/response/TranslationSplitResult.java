package com.cliptripbe.feature.translate.dto.response;

import static com.cliptripbe.global.util.StreamUtils.distinctByKey;

import com.cliptripbe.feature.place.dto.PlaceDto;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record TranslationSplitResult(
    List<TranslatedPlaceAddress> translatedPlaces,
    List<PlaceDto> untranslatedPlaces
) {

    public TranslationSplitResult {
        translatedPlaces = (translatedPlaces == null) ? List.of() : List.copyOf(translatedPlaces);
        untranslatedPlaces = (untranslatedPlaces == null) ? List.of() : List.copyOf(untranslatedPlaces);
    }

    public List<TranslatedPlaceAddress> mergeWith(List<TranslatedPlaceAddress> newTranslations) {
        if (newTranslations == null || newTranslations.isEmpty()) {
            return translatedPlaces;
        }
        return Stream.concat(this.translatedPlaces.stream(), newTranslations.stream())
            .filter(distinctByKey(TranslatedPlaceAddress::getCacheKey))
            .collect(Collectors.toList());
    }

}