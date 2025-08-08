package com.cliptripbe.feature.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;

public record PlaceSearchByCategoryRequest(
    @Schema(description = "대형마트 코드", defaultValue = "MT1")
    String categoryCode,
    @Schema(description = "시청역 위도", defaultValue = "37.5665(시청역 기준)")
    String x,
    @Schema(description = "시청역 경도", defaultValue = "126.978")
    String y,
    @Max(value = 20000, message = "radius는 20000 이하이어야 합니다")
    Integer radius
) {

}
