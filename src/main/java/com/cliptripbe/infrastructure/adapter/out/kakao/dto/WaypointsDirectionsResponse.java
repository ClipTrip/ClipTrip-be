package com.cliptripbe.infrastructure.adapter.out.kakao.dto;

import java.util.List;
import org.springframework.util.RouteMatcher.Route;

public record WaypointsDirectionsResponse(
    String trans_id,
    List<Route> routes
) {

    public record Route(
        int result_code,
        String result_msg,
        Summary summary,
        List<Section> sections
    ) {

    }

    public record Summary(
        OriginDest origin,
        OriginDest destination,
        List<OriginDest> waypoints,
        String priority,
        int distance,
        int duration
    ) {

        public record OriginDest(
            String name,
            double x,
            double y) {

        }
    }

    public record Section(
        int distance,
        int duration
    ) {

    }
}
