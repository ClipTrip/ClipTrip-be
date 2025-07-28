package com.cliptripbe.feature.bookmark.dto.response;

import lombok.Builder;

@Builder
public record BookmarkListResponseDto(
    Long bookmarkId,
    String name,
    String description
) {

}
