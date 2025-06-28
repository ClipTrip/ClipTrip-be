package com.cliptripbe.feature.bookmark.api.dto.response;

import lombok.Builder;

@Builder
public record BookmarkListResponseDto(
    Long bookmarkId,
    String name,
    String description
) {

}
