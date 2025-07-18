package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByCategoryRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByKeywordRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceResponseDto;
import com.cliptripbe.feature.place.domain.vo.LuggageStorageRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Place 관련 API")
public interface PlaceControllerDocs {

    @Operation(summary = "장소 접근성 조회 API, 로그인 불필요")
    ApiResponse<?> getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto
    );

    @Operation(summary = "유저의 장소 카드 정보 조회, 로그인 필요")
    ApiResponse<?> getPlaceById(
        PlaceInfoRequestDto placeInfoRequestDto,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "장소카드 상세조회, 로그인 필요",
        description = "장소 카드 상세 조회입니다. 유저가 해당 장소에 북마크를 했는지 여부를 알려줍니다.")
    ApiResponse<PlaceResponseDto> getPlaceById(
        Long placeId,
        CustomerDetails customerDetails
    );

    @Operation(
        summary = "카테고리 별 장소리스트 조회",
        description = "카테고리를 이용해 현재 장소 정보들을 조회합니다.")
    ApiResponse<List<PlaceListResponseDto>> getPlacesByCategory(
        PlaceSearchByCategoryRequestDto request
    );

    @Operation(
        summary = "키워드 장소리스트 조회",
        description = "키워드를 이용해 현재 장소 정보들을 조회합니다.")
    ApiResponse<List<PlaceListResponseDto>> getPlacesByKeyword(
        PlaceSearchByKeywordRequestDto request
    );

    @Operation(
        summary = "특정 범위 이내의 물품보관소 리스트 조회",
        description = "위도 경도를 이용해 2km 이내 물품 보관소 정보를 조회합니다."
    )
    ApiResponse<List<PlaceListResponseDto>> getLuggageStorages(
        LuggageStorageRequestDto luggageStorageRequestDto
    );
}
