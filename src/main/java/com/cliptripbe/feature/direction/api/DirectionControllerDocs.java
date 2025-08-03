package com.cliptripbe.feature.direction.api;

import com.cliptripbe.feature.direction.dto.request.DirectionsRequest;
import com.cliptripbe.feature.direction.dto.response.DirectionsResponse;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "길찾기 관련 API")
public interface DirectionControllerDocs {

    @Operation(
        summary = "다중 경유지 길찾기",
        description = "출발지, 경유지, 도착지를 넣어 길찾기 좌표값을 조회합니다")
    ApiResponse<DirectionsResponse> getWaypointsDirections(
        DirectionsRequest request
    );
}
