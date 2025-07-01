package com.cliptripbe.feature.place.api.dto.request;

public record PlaceSearchByCategoryRequestDto(
    String categoryCode,
    String x,
    String y,
    Integer radius
) {

}
