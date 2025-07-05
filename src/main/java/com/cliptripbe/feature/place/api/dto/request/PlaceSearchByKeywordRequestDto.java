package com.cliptripbe.feature.place.api.dto.request;

import jakarta.validation.constraints.Max;

public record PlaceSearchByKeywordRequestDto(
    String query,
    String x,
    String y,
    @Max(value = 20000, message = "radius는 20000 이하이어야 합니다")
    Integer radius
) {

}
