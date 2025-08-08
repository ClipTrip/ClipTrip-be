package com.cliptripbe.feature.bookmark.api;


import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.bookmark.application.BookmarkService;
import com.cliptripbe.feature.bookmark.dto.request.CreateBookmarkRequest;
import com.cliptripbe.feature.bookmark.dto.request.UpdateBookmarkRequest;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponse;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkListResponse;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/bookmarks")
public class BookmarkController implements BookmarkControllerDocs {

    private final BookmarkService bookmarkService;

    @Override
    @PostMapping
    public ApiResponse<Long> createBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody CreateBookmarkRequest createBookmarkRequest
    ) {
        Long bookmarkId = bookmarkService.createBookmark(
            customerDetails.getUser(),
            createBookmarkRequest);
        return ApiResponse.success(SuccessType.OK, bookmarkId);
    }

    @Override
    @PatchMapping("/{bookmarkId}")
    public ApiResponse<Long> updateBookmark(
        @PathVariable(value = "bookmarkId") Long bookmarkId,
        @RequestBody @Valid UpdateBookmarkRequest updateBookmarkRequest
    ) {
        bookmarkService.updateBookmark(bookmarkId, updateBookmarkRequest);
        return ApiResponse.success(SuccessType.OK, bookmarkId);
    }

    @Override
    @PostMapping("/{bookmarkId}")
    public ApiResponse<Long> addPlaceToBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId,
        @RequestBody @Valid PlaceInfoRequest placeInfoRequest
    ) {
        bookmarkService.addPlaceToBookmark(
            customerDetails.getUser(),
            bookmarkId,
            placeInfoRequest
        );
        return ApiResponse.success(SuccessType.OK, bookmarkId);
    }

    @Override
    @DeleteMapping("/{bookmarkId}/{placeId}")
    public ApiResponse<Long> deletePlaceFromBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable(value = "bookmarkId") Long bookmarkId,
        @PathVariable(value = "placeId") Long placeId
    ) {
        bookmarkService.deletePlaceFromBookmark(customerDetails.getUser(), bookmarkId, placeId);
        return ApiResponse.success(SuccessType.OK, placeId);
    }


    @Override
    @GetMapping
    public ApiResponse<List<BookmarkListResponse>> getUserBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<BookmarkListResponse> list = bookmarkService.getUserBookmark(
            customerDetails.getUser());
        return ApiResponse.success(SuccessType.OK, list);
    }

    @Override
    @GetMapping("/{bookmarkId}")
    public ApiResponse<BookmarkInfoResponse> getBookmarkById(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId
    ) {
        BookmarkInfoResponse response = bookmarkService.getBookmarkInfo(
            bookmarkId,
            customerDetails.getUser()
        );
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Override
    @DeleteMapping("/{bookmarkId}")
    public ApiResponse<?> deleteBookmark(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long bookmarkId
    ) {
        bookmarkService.deleteBookmark(customerDetails.getUser(), bookmarkId);
        return ApiResponse.success(SuccessType.OK);
    }

//    @GetMapping("/default")
//    public ApiResponse<List<BookmarkListResponseDto>> getDefaultBookmarkList() {
//        List<BookmarkListResponseDto> defaultBookmarkList = bookmarkService.getDefaultBookmarkList();
//        return ApiResponse.success(SuccessType.SUCCESS, defaultBookmarkList);
//    }
}
