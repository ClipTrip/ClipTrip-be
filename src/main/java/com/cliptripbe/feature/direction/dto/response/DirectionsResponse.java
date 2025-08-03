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
            .map(r -> Route.builder()
                .resultCode(r.result_code())
                .resultMsg(r.result_msg())
                .summary(mapSummary(r.summary()))
                .sections(mapSections(r.sections()))
                .build()
            )
            .toList();

        return DirectionsResponse.builder()
            .transId(response.trans_id())
            .routes(routes)
            .build();
    }

    private static Summary mapSummary(WaypointsDirectionsResponse.Summary summary) {
        return Summary.builder()
            .origin(
                Summary.Location.builder()
                    .name(summary.origin().name())
                    .x(summary.origin().x())
                    .y(summary.origin().y())
                    .build()
            )
            .destination(
                Summary.Location.builder()
                    .name(summary.destination().name())
                    .x(summary.destination().x())
                    .y(summary.destination().y())
                    .build()
            )
            .waypoints(
                summary.waypoints().stream()
                    .map(wp -> Summary.Location.builder()
                        .name(wp.name())
                        .x(wp.x())
                        .y(wp.y())
                        .build()
                    )
                    .toList()
            )
            .priority(summary.priority())
            .bound(
                Summary.Bound.builder()
                    .minX(summary.bound().min_x())
                    .minY(summary.bound().min_y())
                    .maxX(summary.bound().max_x())
                    .maxY(summary.bound().max_y())
                    .build()
            )
            .fare(
                Summary.Fare.builder()
                    .taxi(summary.fare().taxi())
                    .toll(summary.fare().toll())
                    .build()
            )
            .distance(summary.distance())
            .duration(summary.duration())
            .build();
    }

    private static List<Section> mapSections(List<WaypointsDirectionsResponse.Section> sections) {
        return sections.stream()
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
    }

}