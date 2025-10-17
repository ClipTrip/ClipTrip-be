package com.cliptripbe.feature.video.api;

import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유튜브 영상 관련 API")
public interface VideoControllerDocs {

    @Operation(summary = "장소 추출하기, \n로그인 필요")
    ApiResponse<VideoScheduleResponse> extractPlacesFromYoutube(
        CustomerDetails customerDetails,
        ExtractPlaceRequest request,

        @Parameter(description = "멱등 키", required = true, example = "391dc8ed-6210-42ed-9eb2-8b2c9cd56542")
        String idempotencyKey
    );
}
