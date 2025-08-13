package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.dto.request.LuggageStorageRequest;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceResponse;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Place 관련 API")
public interface PlaceControllerDocs {

    @Operation(
        summary = "장소카드 상세조회 by placeId, 로그인 필요",
        description = "장소 카드 상세 조회입니다 by placeId. 물품보관소, 북마크, 일정에서 장소 카드로 넘어갈 때 사용됩니다.")
    ApiResponse<PlaceResponse> getPlaceById(
        Long placeId,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "장소카드 상세조회 by kakaoPlaceId, 로그인 필요",
        description = "장소 카드 상세 조회입니다 by kakaoPlaceId. 키워드, 카테고리 검색을 통해 장소 카드로 넘어갈 때 사용됩니다.")
    ApiResponse<PlaceResponse> findOrCreatePlaceByKakaoPlaceId(
        PlaceInfoRequest request,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "카테고리 별 장소리스트 조회",
        description = "카테고리를 이용해 현재 장소 정보들을 조회합니다.")
    ApiResponse<List<PlaceListResponse>> getPlacesByCategory(
        PlaceSearchByCategoryRequest request,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "키워드 장소리스트 조회",
        description = "키워드를 이용해 현재 장소 정보들을 조회합니다.")
    ApiResponse<List<PlaceListResponse>> getPlacesByKeyword(
        PlaceSearchByKeywordRequest request,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "특정 범위 이내의 물품보관소 리스트 조회",
        description = "위도 경도를 이용해 2km 이내 물품 보관소 정보를 조회합니다."
    )
    ApiResponse<List<PlaceListResponse>> getLuggageStorages(
        LuggageStorageRequest luggageStorageRequest,
        CustomerDetails customerDetails
    );
}
