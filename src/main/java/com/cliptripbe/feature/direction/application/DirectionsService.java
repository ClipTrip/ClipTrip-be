package com.cliptripbe.feature.direction.application;

import com.cliptripbe.feature.direction.dto.request.DirectionsRequest;
import com.cliptripbe.feature.direction.dto.response.DirectionsResponse;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;
import com.cliptripbe.infrastructure.port.kakao.RouteSearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectionsService {

    private final RouteSearchPort routeSearchPort;

    public DirectionsResponse getDirections(DirectionsRequest request) {
        WaypointsDirectionsRequest kakaoRequest = request.toWaypointsDirectionsRequest();
        WaypointsDirectionsResponse response = routeSearchPort.getDirections(kakaoRequest);
//        System.out.println("디버깅");
        return DirectionsResponse.from(response);
    }
}
