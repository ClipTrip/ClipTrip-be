package com.cliptripbe.infrastructure.adapter.out.kakao.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record WaypointsDirectionsRequest(
    Location origin,
    Location destination,
    List<Location> waypoints,
    String priority,
    String carFuel,
    boolean carHipass,
    boolean alternatives,
    boolean roadDetails,
    boolean summary,
    Integer roadevent
) {

    @Builder
    public record Location(
        String name,
        double x,
        double y,
        Integer angle
    ) {

    }
}
