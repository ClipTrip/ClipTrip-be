package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByCategoryRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByKeywordRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Place 관련 API")
public interface PlaceControllerDocs {

    @Operation(summary = "장소이름을 받고 그 장소 정보 조회 API")
    PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto);

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
  
    @Operation(summary = "장소 접근성 조회 API, 로그인 불필요")
    ApiResponse<?> getPlaceAccessibilityInfo(
        PlaceInfoRequestDto placeInfoRequestDto
    );

    @Operation(summary = "유저의 장소 카드 정보 조회, 로그인 필요")
    ApiResponse<?> getPlaceInfo(
        PlaceInfoRequestDto placeInfoRequestDto,
        CustomerDetails customerDetails
    );

    @Operation(summary = "유저의 장소 카드 정보 조회 (장소 아이디를 이용해서), 로그인 필요")
    ApiResponse<?> getPlaceInfo(
        Long placeId,
        CustomerDetails customerDetails
    );
}
