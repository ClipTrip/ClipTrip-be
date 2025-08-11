package com.cliptripbe.feature.translate.dto;

import com.cliptripbe.feature.place.domain.vo.PlaceInfoWithTranslation;
import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record TranslationSplitResult(
    List<PlaceInfoWithTranslation> translatedPlaces,
    List<PlaceDto> untranslatedPlaces
) {

    public List<PlaceInfoWithTranslation> mergeWith(List<TranslationInfo> newTranslations,
        Language userLanguage) {

        List<PlaceInfoWithTranslation> newlyTranslatedPlaces = IntStream.range(0,
                untranslatedPlaces.size())
            .mapToObj(i -> PlaceInfoWithTranslation.of(
                untranslatedPlaces.get(i),
                newTranslations.get(i),
                userLanguage
            ))
            .toList();

        List<PlaceInfoWithTranslation> allTranslatedPlaces = new ArrayList<>();
        allTranslatedPlaces.addAll(translatedPlaces);
        allTranslatedPlaces.addAll(newlyTranslatedPlaces);

        return allTranslatedPlaces;
    }

}