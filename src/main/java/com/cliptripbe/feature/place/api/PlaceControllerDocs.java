package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Place 관련 API")
public interface PlaceControllerDocs {

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
