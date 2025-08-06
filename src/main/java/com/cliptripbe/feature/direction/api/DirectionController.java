package com.cliptripbe.feature.direction.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.direction.application.DirectionsService;
import com.cliptripbe.feature.direction.dto.request.DirectionsRequest;
import com.cliptripbe.feature.direction.dto.response.DirectionsResponse;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/directions")
public class DirectionController implements DirectionControllerDocs {

    private final DirectionsService directionsService;

    @Override
    @PostMapping("/waypoints")
    public ApiResponse<DirectionsResponse> getWaypointsDirections(
        @Valid @RequestBody DirectionsRequest request
    ) {
        DirectionsResponse response = directionsService.getDirections(request);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
