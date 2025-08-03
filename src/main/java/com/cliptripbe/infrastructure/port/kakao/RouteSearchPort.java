package com.cliptripbe.infrastructure.port.kakao;

import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;

public interface RouteSearchPort {

    WaypointsDirectionsResponse getDirections(WaypointsDirectionsRequest request);

}
