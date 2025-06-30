package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
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
        @RequestParam("categoryCode") String categoryCode,
        @RequestParam String x,
        @RequestParam String y,
        @RequestParam Integer radius
    );
}
