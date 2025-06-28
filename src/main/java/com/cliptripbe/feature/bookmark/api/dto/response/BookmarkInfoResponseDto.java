package com.cliptripbe.feature.bookmark.api.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import java.util.List;
import lombok.Builder;

@Builder
public record BookmarkInfoResponseDto(
    Long id,
    String name,
    String description,
    List<Place> placeList
) {

}
