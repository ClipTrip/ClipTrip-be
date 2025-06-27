package com.cliptripbe.feature.video.api;

import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.feature.video.application.VideoService;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController implements VideoControllerDocs {

    private final VideoService videoService;

    @PostMapping()
    public ApiResponse<?> extractPlacesFromYoutube(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody ExtractPlaceRequestDto request
    ) {
        videoService.extractPlace(customerDetails.getUser(), request);
        return ApiResponse.success(SuccessType.CREATED);
    }
}
