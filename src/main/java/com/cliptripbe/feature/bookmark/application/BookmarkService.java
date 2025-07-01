package com.cliptripbe.feature.bookmark.application;

import com.cliptripbe.feature.bookmark.api.dto.request.CreateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.request.UpdateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkInfoResponseDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkListResponseDto;
import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.application.PlaceFinder;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    final PlaceFinder placeFinder;
    final BookmarkRepository bookmarkRepository;
    final BookmarkFinder bookmarkFinder;

    @Transactional
    public Long createBookmark(User user, CreateBookmarkRequestDto request) {
        Bookmark bookmark = Bookmark
            .builder()
            .name(request.bookmarkName())
            .description(request.description())
            .user(user)
            .build();
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Transactional
    public void updateBookmark(
        Long bookmarkId,
        UpdateBookmarkRequestDto updateBookmarkRequestDto
    ) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
        bookmark.cleanBookmarkPlace();
        for (PlaceInfoRequestDto placeInfoRequestDto : updateBookmarkRequestDto.placeInfoRequestDtos()) {
            BookmarkPlace bookmarkPlace = BookmarkPlace
                .builder()
                .bookmark(bookmark)
                .place(placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto))
                .build();
            bookmark.addBookmarkPlace(bookmarkPlace);
        }
    }

    @Transactional
    public void addBookmark(User user, Long bookmarkId, PlaceInfoRequestDto placeInfoRequestDto) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        BookmarkPlace bookmarkPlace = BookmarkPlace
            .builder()
            .bookmark(bookmark)
            .place(placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto))
            .build();
        bookmark.addBookmarkPlace(bookmarkPlace);
    }

    public List<BookmarkListResponseDto> getUserBookmark(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks
            .stream()
            .map(BookmarkMapper::mapBookmarkListResponseDto)
            .toList();
    }

    public BookmarkInfoResponseDto getBookmarkInfo(Long bookmarkId) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
        return BookmarkMapper.mapBookmarkInfoResponse(bookmark);
    }

    @Transactional
    public void deleteBookmark(User user, Long bookmarkId) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        bookmarkRepository.delete(bookmark);
    }

    public List<BookmarkListResponseDto> getDefaultBookmarkList() {
        List<Bookmark> defaultBookmark = bookmarkFinder.getDefaultBookmark();
        return defaultBookmark.stream()
            .map(BookmarkMapper::mapBookmarkListResponseDto)
            .toList();
    }
}
