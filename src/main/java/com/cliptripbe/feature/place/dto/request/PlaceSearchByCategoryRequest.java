package com.cliptripbe.feature.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;

public record PlaceSearchByCategoryRequest(
    @Schema(description = "카테고리 그룹 코드 (예: MT1 – 대형마트)", defaultValue = "MT1")
    String categoryCode,
    @Schema(description = "시청역 위도 (공공데이터 표준)", defaultValue = "37.5644")
    String x,
    @Schema(description = "시청역 경도 (공공데이터 표준)", defaultValue = "126.9771")
    String y,
    @Max(value = 20000, message = "radius는 20000 이하이어야 합니다")
    Integer radius
) {

}
