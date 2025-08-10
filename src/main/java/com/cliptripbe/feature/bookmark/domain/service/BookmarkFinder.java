package com.cliptripbe.feature.bookmark.domain.service;

import static com.cliptripbe.global.initialization.type.DefaultData.STORAGE_SEOUL;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.bookmark.infrastructure.projection.BookmarkMapping;
import com.cliptripbe.feature.bookmark.infrastructure.projection.PlaceBookmarkMapping;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFinder {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findByIdWithBookmarkPlaces(bookmarkId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    public List<Bookmark> getDefaultBookmarksExcludingStorageSeoul() {
        return bookmarkRepository.findDefaultBookmarksExcludingName(STORAGE_SEOUL.getName());
    }

    public Bookmark findByIdWithPlacesAndTranslations(Long bookmarkId) {
        return bookmarkRepository.findByIdWithPlacesAndTranslations(bookmarkId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    public Map<String, List<Long>> findBookmarkIdsByKakaoPlaceIds(
        Long userId,
        List<String> kakaoPlaceIdList
    ) {
        if (kakaoPlaceIdList == null || kakaoPlaceIdList.isEmpty()) {
            return Map.of();
        }
        // String : kakaoPlaceId , List<Long> : 북마크 ids
        return bookmarkRepository.findBookmarkMappingsByUserAndKakaoPlaceIds(
                userId, kakaoPlaceIdList).stream()
            .collect(Collectors.groupingBy(
                BookmarkMapping::getKakaoPlaceId,
                Collectors.mapping(BookmarkMapping::getBookmarkId, Collectors.toList())
            ));
    }

    public Map<Long, List<Long>> findBookmarkIdsByPlaceIds(Long userId, List<Long> placeIdList) {
        if (placeIdList == null || placeIdList.isEmpty()) {
            return Map.of();
        }
        // Long : placeId , List<Long> : 북마크 ids
        List<PlaceBookmarkMapping> mappings = bookmarkRepository.findPlaceBookmarkMappingsByUserAndPlaceIds(
            placeIdList, userId);
        return mappings.stream()
            .collect(Collectors.groupingBy(
                PlaceBookmarkMapping::getPlaceId,
                Collectors.mapping(PlaceBookmarkMapping::getBookmarkId, Collectors.toList())
            ));
    }
}
