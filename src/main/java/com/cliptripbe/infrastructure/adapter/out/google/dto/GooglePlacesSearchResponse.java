package com.cliptripbe.infrastructure.adapter.out.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GooglePlacesSearchResponse(
    List<TextSearchResult> results
) {

    public record TextSearchResult(
        @JsonProperty("place_id")
        String placeId,
        List<PhotoDto> photos
    ) {

    }

    public record PhotoDto(
        @JsonProperty("photo_reference")
        String photoReference
    ) {

    }
}
