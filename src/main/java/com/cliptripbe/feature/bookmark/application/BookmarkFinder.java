package com.cliptripbe.feature.bookmark.application;

import static com.cliptripbe.infrastructure.inital.type.DefaultData.STORAGE_SEOUL;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
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
}
