package com.cliptripbe.feature.video.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.video.application.VideoPlaceExtractFacade;
import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.aop.idempotency.annotation.Idempotent;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_VERSION + "/videos")
@RequiredArgsConstructor
public class VideoController implements VideoControllerDocs {

    private final VideoPlaceExtractFacade videoPlaceExtractFacade;

    @Idempotent
    @PostMapping()
    public ApiResponse<VideoScheduleResponse> extractPlacesFromYoutube(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @Valid @RequestBody ExtractPlaceRequest request,
        @RequestHeader("Idempotency-Key") String idempotencyKey
    ) {
        VideoScheduleResponse videoScheduleResponse = videoPlaceExtractFacade.extractPlace(
            customerDetails.getUser(),
            request
        );
        return ApiResponse.success(SuccessType.OK, videoScheduleResponse);
    }
}
