package com.cliptripbe.feature.direction.application;

import com.cliptripbe.feature.direction.dto.request.DirectionsRequest;
import com.cliptripbe.feature.direction.dto.response.DirectionsResponse;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;
import com.cliptripbe.infrastructure.port.kakao.KakaoMobilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectionsService {

    private final KakaoMobilityPort kakaoMobilityPort;

    public DirectionsResponse getDirections(DirectionsRequest request) {
        WaypointsDirectionsRequest kakaoRequest = request.toWaypointsDirectionsRequest();
        WaypointsDirectionsResponse response = kakaoMobilityPort.getDirections(kakaoRequest);
        return DirectionsResponse.from(response);
    }
}
