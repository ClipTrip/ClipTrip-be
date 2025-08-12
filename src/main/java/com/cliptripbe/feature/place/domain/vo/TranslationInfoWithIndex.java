package com.cliptripbe.feature.place.domain.vo;

public record TranslationInfoWithIndex(
    Integer index,
    String translatedName,
    String translatedRoadAddress
) {

}
