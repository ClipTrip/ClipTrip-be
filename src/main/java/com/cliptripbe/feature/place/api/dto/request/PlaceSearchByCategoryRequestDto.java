package com.cliptripbe.feature.place.api.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

public record PlaceSearchByCategoryRequestDto(
    String categoryCode,
    String x,
    String y,
    Integer radius
) {

}
