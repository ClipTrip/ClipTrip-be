package com.cliptripbe.feature.place.api.dto.request;

public record PlaceSearchByKeywordRequestDto(
    String query,
    String x,
    String y,
    Integer radius
) {

}
