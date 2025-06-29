package com.cliptripbe.feature.bookmark.api;



import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.bookmark.api.dto.request.CreateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.request.DeletePlaceInBookmark;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkInfoResponseDto;
import com.cliptripbe.feature.bookmark.api.dto.response.BookmarkListResponseDto;
import com.cliptripbe.feature.bookmark.application.BookmarkService;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/bookmark")
public class BookmarkController implements BookmarkControllerDocs {

    final BookmarkService bookmarkService;

    @Override
    @PostMapping
    public ApiResponse<?> createBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody CreateBookmarkRequestDto createBookmarkRequestDto
    ) {
        Long bookmarkId = bookmarkService.createBookmark(customerDetails.getUser(),
            createBookmarkRequestDto);
        return ApiResponse.success(SuccessType.SUCCESS, bookmarkId);
    }

    @Override
    @PostMapping("/{bookmarkId}")
    public ApiResponse<?> addBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId,
        @RequestBody PlaceInfoRequestDto placeInfoRequestDto
    ) {
        bookmarkService.addBookmark(
            customerDetails.getUser(),
            bookmarkId,
            placeInfoRequestDto
        );
        return ApiResponse.success(SuccessType.SUCCESS, bookmarkId);
    }

    @Override
    @GetMapping
    public ApiResponse<?> getUserBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<BookmarkListResponseDto> list = bookmarkService.getUserBookmark(
            customerDetails.getUser());
        return ApiResponse.success(SuccessType.SUCCESS, list);
    }

    @Override
    @GetMapping("/{bookmarkId}")
    public ApiResponse<?> getBookmarkInfo(
        @PathVariable Long bookmarkId
    ) {
        BookmarkInfoResponseDto responseDto = bookmarkService.getBookmarkInfo(bookmarkId);
        return ApiResponse.success(SuccessType.SUCCESS, responseDto);
    }

    @Override
    @DeleteMapping("/{bookmarkId}/places")
    public ApiResponse<?> deletePlaceInBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId,
        @RequestBody DeletePlaceInBookmark deletePlaceInBookmark
    ) {
        bookmarkService.deletePlaceInBookmark(
            bookmarkId,
            deletePlaceInBookmark.placeId(),
            customerDetails.getUser());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @Override
    @DeleteMapping("/{bookmarkId}")
    public ApiResponse<?> deleteBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId
    ) {
        bookmarkService.deleteBookmark(customerDetails.getUser(), bookmarkId);
        return ApiResponse.success(SuccessType.SUCCESS);
    }
}
