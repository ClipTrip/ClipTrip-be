package com.cliptripbe.feature.bookmark.api.dto.request;

public record CreateBookmarkRequestDto(
    String bookmarkName,
    String description
) {

}
