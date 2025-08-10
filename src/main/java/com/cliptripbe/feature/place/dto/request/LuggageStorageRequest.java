package com.cliptripbe.feature.place.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record LuggageStorageRequest(
    @NotNull(message = "위도는 null일 수 없습니다.")
    @DecimalMin(value = "33.0", message = "위도는 33.0 이상이어야 합니다.")
    @DecimalMax(value = "38.7", message = "위도는 38.7 이하이어야 합니다.")
    Double latitude,

    @NotNull(message = "경도는 null일 수 없습니다.")
    @DecimalMin(value = "124.0", message = "경도는 124.0 이상이어야 합니다.")
    @DecimalMax(value = "132.0", message = "경도는 132.0 이하이어야 합니다.")
    Double longitude
) {

}
