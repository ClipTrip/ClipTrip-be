package com.cliptripbe.feature.bookmark.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.domain.service.BookmarkFinder;
import com.cliptripbe.feature.bookmark.domain.service.BookmarkMapper;
import com.cliptripbe.feature.bookmark.dto.request.CreateBookmarkRequest;
import com.cliptripbe.feature.bookmark.dto.request.UpdateBookmarkRequest;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponse;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkListResponse;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.Set;
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

    @Transactional
    public Long createBookmark(
        User user,
        CreateBookmarkRequest request
    ) {
        Bookmark bookmark = Bookmark.createByUser(request.bookmarkName(), request.description(),
            user);

        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Transactional
    public void updateBookmark(
        Long bookmarkId,
        UpdateBookmarkRequest updateBookmarkRequest
    ) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        bookmark.modifyInfo(updateBookmarkRequest.bookmarkName(),
            updateBookmarkRequest.description());
        bookmark.cleanBookmarkPlace();

        for (PlaceInfoRequest placeInfoRequest : updateBookmarkRequest.placeInfoRequests()) {
            Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequest);
            BookmarkPlace bookmarkPlace = BookmarkPlace
                .builder()
                .bookmark(bookmark)
                .place(place)
                .build();
            bookmark.addBookmarkPlace(bookmarkPlace);
        }
    }

    @Transactional
    public void addBookmark(User user, Long bookmarkId, PlaceInfoRequest placeInfoRequest) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequest);

        BookmarkPlace bookmarkPlace = BookmarkPlace
            .builder()
            .bookmark(bookmark)
            .place(place)
            .build();
        bookmark.addBookmarkPlace(bookmarkPlace);
    }

    @Transactional(readOnly = true)
    public List<BookmarkListResponse> getUserBookmark(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks
            .stream()
            .map(BookmarkMapper::mapBookmarkListResponseDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public BookmarkInfoResponse getBookmarkInfo(Long bookmarkId, User user) {

        if (user.getLanguage() == KOREAN) {
            Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
            return BookmarkMapper.mapBookmarkInfoResponse(bookmark);
        }
        Bookmark bookmark = bookmarkFinder.findByIdWithPlacesAndTranslations(bookmarkId);

        Set<BookmarkPlace> bookmarkPlaces = bookmark.getBookmarkPlaces();

        List<PlaceListResponse> placeListResponses = bookmarkPlaces.stream()
            .map(bp -> {
                Place place = bp.getPlace();
                PlaceTranslation placeTranslation = place.getTranslationByLanguage(
                    user.getLanguage());
                return PlaceListResponse.of(place, placeTranslation, -1);
            })
            .collect(Collectors.toList());

        return BookmarkMapper.mapBookmarkInfoResponse(bookmark, placeListResponses);
    }

    @Transactional
    public void deleteBookmark(User user, Long bookmarkId) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);
        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        bookmarkRepository.delete(bookmark);
    }
}
