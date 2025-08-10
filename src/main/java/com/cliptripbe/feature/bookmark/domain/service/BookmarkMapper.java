package com.cliptripbe.feature.bookmark.domain.service;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponse;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import java.util.List;

public class BookmarkMapper {

    public static BookmarkListResponse mapBookmarkListResponseDto(Bookmark bookmark) {
        return BookmarkListResponse.builder()
            .bookmarkId(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .build();
    }
}
