package com.cliptripbe.feature.video.api;

import com.cliptripbe.feature.schedule.api.dto.request.CreateScheduleRequestDto;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유튜브 영상 관련 API")
public interface VideoControllerDocs {

    @Operation(summary = "장소 추출하기, \n로그인 필요")
    ApiResponse<?> extractPlacesFromYoutube(
        CustomerDetails customerDetails,
        ExtractPlaceRequestDto request
    );
}
