package com.cliptripbe.feature.bookmark.dto.response;

import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record BookmarkInfoResponse(
    Long id,
    String name,
    String description,
    List<PlaceListResponse> placeList
) {

}
