package com.cliptripbe.feature.bookmark.dto.response;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
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

    public static BookmarkInfoResponse from(Bookmark bookmark) {
        return BookmarkInfoResponse.builder()
            .id(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .placeList(
                bookmark.getPlaces().stream()
                    .limit(30)
                    .map(place -> PlaceListResponse.fromEntity(place, -1))
                    .toList()
            )
            .build();
    }
}
