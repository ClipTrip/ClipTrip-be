package com.cliptripbe.feature.direction.dto.response;

import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record DirectionsResponse(
    String transId,
    List<Route> routes
) {

    @Builder
    public record Route(
        int resultCode,
        String resultMsg,
        Summary summary,
        List<Section> sections
    ) {

    }

    @Builder
    public record Summary(
        Location origin,
        Location destination,
        List<Location> waypoints,
        String priority,
        Bound bound,
        Fare fare,
        int distance,
        int duration
    ) {

        @Builder
        public record Location(
            String name,
            double x,
            double y
        ) {

        }

        @Builder
        public record Bound(
            double minX,
            double minY,
            double maxX,
            double maxY
        ) {

        }

        @Builder
        public record Fare(
            int taxi,
            int toll
        ) {

        }
    }

    @Builder
    public record Section(
        int distance,
        int duration,
        Bound bound,
        List<Road> roads
    ) {

        @Builder
        public record Bound(
            double minX,
            double minY,
            double maxX,
            double maxY
        ) {

        }

        @Builder
        public record Road(
            String name,
            int distance,
            int duration,
            double trafficSpeed,
            int trafficState,
            List<Double> vertexes
        ) {

        }

    }

    public static DirectionsResponse from(WaypointsDirectionsResponse response) {
        List<Route> routes = response.routes().stream()
            .map(r -> {
                // map Summary
                Summary summary = Summary.builder()
                    .origin(
                        Summary.Location.builder()
                            .name(r.summary().origin().name())
                            .x(r.summary().origin().x())
                            .y(r.summary().origin().y())
                            .build()
                    )
                    .destination(
                        Summary.Location.builder()
                            .name(r.summary().destination().name())
                            .x(r.summary().destination().x())
                            .y(r.summary().destination().y())
                            .build()
                    )
                    .waypoints(
                        r.summary().waypoints().stream()
                            .map(wp -> Summary.Location.builder()
                                .name(wp.name())
                                .x(wp.x())
                                .y(wp.y())
                                .build()
                            )
                            .toList()
                    )
                    .priority(r.summary().priority())
                    .bound(
                        Summary.Bound.builder()
                            .minX(r.summary().bound().min_x())
                            .minY(r.summary().bound().min_y())
                            .maxX(r.summary().bound().max_x())
                            .maxY(r.summary().bound().max_y())
                            .build()
                    )
                    .fare(
                        Summary.Fare.builder()
                            .taxi(r.summary().fare().taxi())
                            .toll(r.summary().fare().toll())
                            .build()
                    )
                    .distance(r.summary().distance())
                    .duration(r.summary().duration())
                    .build();

                // map Sections
                List<Section> sections = r.sections().stream()
                    .map(s -> Section.builder()
                        .distance(s.distance())
                        .duration(s.duration())
                        .bound(
                            Section.Bound.builder()
                                .minX(s.bound().min_x())
                                .minY(s.bound().min_y())
                                .maxX(s.bound().max_x())
                                .maxY(s.bound().max_y())
                                .build()
                        )
                        .roads(
                            // if you have roads in your infrastructure DTO
                            s.roads().stream()
                                .map(rd -> Section.Road.builder()
                                    .name(rd.name())
                                    .distance(rd.distance())
                                    .duration(rd.duration())
                                    .trafficSpeed(rd.traffic_speed())
                                    .trafficState(rd.traffic_state())
                                    .vertexes(rd.vertexes())
                                    .build()
                                )
                                .toList()
                        )
                        .build()
                    )
                    .toList();

                // build Route
                return Route.builder()
                    .resultCode(r.result_code())
                    .resultMsg(r.result_msg())
                    .summary(summary)
                    .sections(sections)
                    .build();
            })
            .toList();

        return DirectionsResponse.builder()
            .transId(response.trans_id())
            .routes(routes)
            .build();
    }

}