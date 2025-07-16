package com.cliptripbe.feature.bookmark.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;

import com.cliptripbe.feature.bookmark.api.dto.request.CreateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.request.UpdateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkInfoResponseDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkListResponseDto;
import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.application.PlaceTranslationFinder;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFinder bookmarkFinder;
    private final PlaceService placeService;
    private final PlaceTranslationFinder placeTranslationFinder;

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

        bookmark.modifyInfo(updateBookmarkRequestDto.bookmarkName(),
            updateBookmarkRequestDto.description());
        bookmark.cleanBookmarkPlace();

        for (PlaceInfoRequestDto placeInfoRequestDto : updateBookmarkRequestDto.placeInfoRequestDtos()) {
            Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequestDto);
            BookmarkPlace bookmarkPlace = BookmarkPlace
                .builder()
                .bookmark(bookmark)
                .place(place)
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
        Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequestDto);

        BookmarkPlace bookmarkPlace = BookmarkPlace
            .builder()
            .bookmark(bookmark)
            .place(place)
            .build();
        bookmark.addBookmarkPlace(bookmarkPlace);
    }

    @Transactional(readOnly = true)
    public List<BookmarkListResponseDto> getUserBookmark(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks
            .stream()
            .map(BookmarkMapper::mapBookmarkListResponseDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public BookmarkInfoResponseDto getBookmarkInfo(Long bookmarkId, User user) {

        if (user.getLanguage() == KOREAN) {
            Bookmark bookmark = bookmarkRepository.findByIdWithBookmarkPlaces(bookmarkId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
            return BookmarkMapper.mapBookmarkInfoResponse(bookmark);
        }
        Bookmark bookmark = bookmarkRepository.findByIdWithPlacesAndTranslations(bookmarkId)
            .orElseThrow(() -> new IllegalArgumentException("Bookmark not found")); // 적절한 예외 처리

        List<BookmarkPlace> bookmarkPlaces = bookmark.getBookmarkPlaces();

        List<PlaceListResponseDto> placeListResponseDtos = bookmarkPlaces.stream()
            .map(bp -> {
                Place place = bp.getPlace();
                PlaceTranslation placeTranslation = place.getPlaceTranslations().stream()
                    .filter(pt -> pt.getLanguage() == user.getLanguage())
                    .findFirst()
                    .orElse(null);

                return PlaceListResponseDto.of(place, placeTranslation, -1);
            })
            .collect(Collectors.toList());

        return BookmarkMapper.mapBookmarkInfoResponse(bookmark, placeListResponseDtos);
    }

    @Transactional
    public void deleteBookmark(User user, Long bookmarkId) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkListResponseDto> getDefaultBookmarkList() {
        List<Bookmark> defaultBookmark = bookmarkFinder.getDefaultBookmark();
        return defaultBookmark.stream()
            .map(BookmarkMapper::mapBookmarkListResponseDto)
            .toList();
    }
}
