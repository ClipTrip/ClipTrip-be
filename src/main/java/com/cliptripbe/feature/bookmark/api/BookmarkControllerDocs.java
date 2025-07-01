package com.cliptripbe.feature.bookmark.api;

import com.cliptripbe.feature.bookmark.api.dto.request.CreateBookmarkRequestDto;
import com.cliptripbe.feature.bookmark.api.dto.request.UpdateBookmarkRequestDto;
import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "북마크 관련 API")
public interface BookmarkControllerDocs {

    @Operation(summary = "북마크 만들기, \n로그인 필요")
    ApiResponse<?> createBookmark(CustomerDetails customerDetails,
        CreateBookmarkRequestDto CreateBookmarkRequestDto);

    @Operation(summary = "북마크 수정하기, \n로그인 필요,put 메서드")
    ApiResponse<?> updateBookmark(
        Long bookmarkId,
        UpdateBookmarkRequestDto updateBookmarkRequestDto
    );

    @Operation(summary = "북마크에 하나의 장소 추가하기, \n로그인 필요")
    ApiResponse<?> addBookmark(
        CustomerDetails customerDetails,
        Long bookmarkId,
        PlaceInfoRequestDto placeInfoRequestDto
    );

    @Operation(summary = "유저 북마크 전체 조회하기, \n로그인 필요")
    ApiResponse<?> getUserBookmark(CustomerDetails customerDetails);

    @Operation(summary = "북마크안의 장소 리스트 조회하기, \n로그인 필요")
    ApiResponse<?> getBookmarkInfo(Long bookmarkId);

    @Operation(summary = "북마크 삭제하기, \n로그인 필요")
    ApiResponse<?> deleteBookmark(
        CustomerDetails customerDetails,
        Long bookmarkId
    );
}
