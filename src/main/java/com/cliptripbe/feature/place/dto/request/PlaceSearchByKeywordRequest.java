package com.cliptripbe.feature.place.dto.request;

import jakarta.validation.constraints.Max;

public record PlaceSearchByKeywordRequest(
    String query,
    String longitude,
    String latitude,
    @Max(value = 20000, message = "radius는 20000 이하이어야 합니다")
    Integer radius
) {

}
