package com.cliptripbe.feature.bookmark.dto.response;

import lombok.Builder;

@Builder
public record BookmarkListResponse(
    Long bookmarkId,
    String name,
    String description
) {

}
