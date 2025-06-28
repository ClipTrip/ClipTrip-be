package com.cliptripbe.feature.bookmark.application;

import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkInfoResponseDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkListResponseDto;
import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;

public class BookmarkMapper {

    public static BookmarkListResponseDto mapBookmarkListResponseDto(Bookmark bookmark) {
        return BookmarkListResponseDto
            .builder()
            .bookmarkId(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .build();
    }

    public static BookmarkInfoResponseDto mapBookmarkInfoResponse(Bookmark bookmark) {
        return BookmarkInfoResponseDto
            .builder()
            .id(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .placeList(bookmark.getPlaces())
            .build();
    }
}
