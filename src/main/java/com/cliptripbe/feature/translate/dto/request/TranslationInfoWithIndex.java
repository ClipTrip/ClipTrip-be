package com.cliptripbe.feature.translate.dto.request;

public record TranslationInfoWithIndex(
    Integer index,
    String translatedName,
    String translatedRoadAddress
) {

}
