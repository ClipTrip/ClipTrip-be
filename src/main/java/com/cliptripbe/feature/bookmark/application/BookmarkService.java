package com.cliptripbe.feature.bookmark.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;
import static com.cliptripbe.global.response.type.ErrorType.ACCESS_DENIED_EXCEPTION;
import static com.cliptripbe.global.response.type.ErrorType.PLACE_NOT_FOUND;

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
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.global.auth.security.CustomerDetails;
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
    public void updateBookmark(Long bookmarkId, UpdateBookmarkRequest request) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        String newName =
            request.bookmarkName() != null ? request.bookmarkName() : bookmark.getName();
        String newDescription =
            request.description() != null ? request.description() : bookmark.getDescription();
        if (request.bookmarkName() != null || request.description() != null) {
            bookmark.modifyInfo(newName, newDescription);
        }

        if (request.placeInfoRequests() != null) {
            bookmark.cleanBookmarkPlace();
            for (PlaceInfoRequest placeInfoRequest : request.placeInfoRequests()) {
                Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequest);
                BookmarkPlace bookmarkPlace = BookmarkPlace.builder()
                    .bookmark(bookmark)
                    .place(place)
                    .build();
                bookmark.addBookmarkPlace(bookmarkPlace);
            }
        }
    }

    @Transactional
    public void addPlaceToBookmark(User user, Long bookmarkId, PlaceInfoRequest placeInfoRequest) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ACCESS_DENIED_EXCEPTION);
        }

        Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequest);

        BookmarkPlace bookmarkPlace = BookmarkPlace.builder()
            .bookmark(bookmark)
            .place(place)
            .build();
        bookmark.addBookmarkPlace(bookmarkPlace);
    }

    @Transactional
    public void deletePlaceFromBookmarkByPlaceId(User user, Long bookmarkId, Long placeId) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ACCESS_DENIED_EXCEPTION);
        }

        BookmarkPlace targetPlace = bookmark.getBookmarkPlaces().stream()
            .filter(bp -> bp.getPlace().getId().equals(placeId))
            .findFirst()
            .orElseThrow(() -> new CustomException(PLACE_NOT_FOUND));

        bookmark.getBookmarkPlaces().remove(targetPlace);
    }

    @Transactional
    public void deletePlaceFromBookmarkByKakaoPlaceId(
        User user,
        Long bookmarkId,
        String kakaoPlaceId
    ) {
        Bookmark bookmark = bookmarkFinder.findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ACCESS_DENIED_EXCEPTION);
        }

        BookmarkPlace targetPlace = bookmark.getBookmarkPlaces().stream()
            .filter(bp -> {
                if (bp.getPlace() == null) {
                    return false;
                }
                String placeKakaoId = bp.getPlace().getKakaoPlaceId();
                return placeKakaoId != null && placeKakaoId.equals(kakaoPlaceId);
            })
            .findFirst()
            .orElseThrow(() -> new CustomException(PLACE_NOT_FOUND));

        bookmark.getBookmarkPlaces().remove(targetPlace);
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
            return BookmarkInfoResponse.from(bookmark);
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
            throw new CustomException(ACCESS_DENIED_EXCEPTION);
        }
        bookmarkRepository.delete(bookmark);
    }


}
