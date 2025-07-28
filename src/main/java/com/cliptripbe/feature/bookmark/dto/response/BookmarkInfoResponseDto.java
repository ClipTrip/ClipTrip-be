package com.cliptripbe.feature.bookmark.dto.response;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record BookmarkInfoResponseDto(
    Long id,
    String name,
    String description,
    List<PlaceListResponseDto> placeList
) {

}
