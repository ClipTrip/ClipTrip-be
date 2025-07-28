package com.cliptripbe.feature.bookmark.domain.service;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponseDto;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkListResponseDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import java.util.List;

public class BookmarkMapper {

    public static BookmarkListResponseDto mapBookmarkListResponseDto(Bookmark bookmark) {
        return BookmarkListResponseDto.builder()
            .bookmarkId(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .build();
    }

    public static BookmarkInfoResponseDto mapBookmarkInfoResponse(
        Bookmark bookmark
    ) {
        return BookmarkInfoResponseDto.builder()
            .id(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .placeList(
                bookmark.getPlaces().stream()
                    .limit(10)
                    .map(place -> PlaceListResponseDto.fromEntity(place, -1))
                    .toList()
            )
            .build();
    }

    public static BookmarkInfoResponseDto mapBookmarkInfoResponse(
        Bookmark bookmark,
        List<PlaceListResponseDto> placeListResponseDtos
    ) {
        return BookmarkInfoResponseDto.builder()
            .id(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .placeList(
                placeListResponseDtos
            )
            .build();
    }
}
