package com.cliptripbe.feature.translate.dto.response;

import static com.cliptripbe.global.util.StreamUtils.distinctByKey;

import com.cliptripbe.feature.place.dto.PlaceDto;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* 번역이 된 것과 안된 것을 갖는 클래스입니다. 몰론 하나의 리스트에 번역된 정보가 없다면 번역이 되지 않은 장소로 알 수 있지만,
안된 부분만을 번역시키고 후에 합치는 로직을 타는 것이 별로라고 생각하여 분리하여 갖도록 했습니다.
 */
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