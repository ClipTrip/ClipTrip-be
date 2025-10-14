package com.cliptripbe.feature.bookmark.application;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponse;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BookmarkResponseAssembler {

    public BookmarkInfoResponse createBookmarkResponseForKorean(
        Bookmark bookmarkWithPlace,
        Map<Long, List<Long>> bookmarkIdsMap,
        User user
    ) {
        List<PlaceListResponse> placeListResponses = bookmarkWithPlace.getPlaces().stream()
            .map(place -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(place.getId(), List.of());
                return PlaceListResponse.ofEntity(place, user.getLanguage(), bookmarkIds);
            })
            .toList();
        return BookmarkInfoResponse.of(bookmarkWithPlace, placeListResponses);
    }
}
