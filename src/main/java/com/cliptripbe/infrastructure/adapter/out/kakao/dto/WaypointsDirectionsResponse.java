package com.cliptripbe.infrastructure.adapter.out.kakao.dto;

import java.util.List;

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
        Bound bound,
        Fare fare,
        int distance,
        int duration
    ) {

        public record OriginDest(
            String name,
            double x,
            double y
        ) {

        }
    }

    public record Section(
        int distance,
        int duration,
        Bound bound,
        List<Road> roads,
        List<Guide> guides
    ) {

    }

    public record Bound(
        double min_x,
        double min_y,
        double max_x,
        double max_y
    ) {

    }

    public record Fare(
        int taxi,
        int toll
    ) {

    }

    public record Road(
        String name,
        int distance,
        int duration,
        double traffic_speed,
        int traffic_state,
        List<Double> vertexes
    ) {

    }

    public record Guide(
        String name,
        double x,
        double y,
        int distance,
        int duration,
        int type,
        String guidance,
        int road_index
    ) {

    }

}
