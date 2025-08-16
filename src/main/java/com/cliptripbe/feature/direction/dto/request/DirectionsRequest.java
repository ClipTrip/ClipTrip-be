package com.cliptripbe.feature.direction.dto.request;

import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DirectionsRequest(
    @Schema(description = "출발 장소 명 ", example = "연세대학교")
    String originName,

    @Schema(description = "출발지 위도", example = "37.56578")
    @NotNull(message = "출발지 위도는 필수입니다")
    Double originLatitude,

    @Schema(description = "출발지 경도", example = "126.9386")
    @NotNull(message = "출발지 경도는 필수입니다")
    Double originLongitude,

    @Schema(description = "도착 장소 명 ", example = "서울대학교")
    String destinationName,

    @Schema(description = "도착지 위도", example = "37.45988")
    @NotNull(message = "도착지 위도는 필수입니다")
    Double destinationLatitude,

    @Schema(description = "도착지 경도", example = "126.9519")
    @NotNull(message = "도착지 경도는 필수입니다")
    Double destinationLongitude,

    @Schema(description = "경유지 목록 (최대 30개)")
    List<Waypoint> wayPoints

//    // 경로 탐색 우선순위
//    String priority,
//
//    // 차량 유종 정보
//    String carFuel,
//
//    // 하이패스 장착 여부
//    boolean carHipass,
//
//    // 대난 경로 제공 여부
//    boolean alternatives,
//
//    // 상세 도로 정보 제공 여부
//    boolean roadDetails,
//
//    // 요약 정보만 제공 여부
//    boolean summary
) {

    public record Waypoint(

        @Schema(description = "경유 장소 명 ", example = "고려대학교")
        String name,

        @Schema(description = "경유지 위도", example = "37.59080")
        @NotNull(message = "경유지 위도는 필수입니다")
        Double latitude,

        @Schema(description = "경유지 경도", example = "127.0278")
        @NotNull(message = "경유지 경도는 필수입니다")
        Double longitude
    ) {

    }

    public WaypointsDirectionsRequest toWaypointsDirectionsRequest() {
        Location origin = Location.builder()
            .name(originName)
            .x(originLongitude)
            .y(originLatitude)
            .angle(null)
            .build();

        Location destination = Location.builder()
            .name(destinationName)
            .x(destinationLongitude)
            .y(destinationLatitude)
            .angle(null)
            .build();

        List<Location> waypoints = wayPoints.stream()
            .map(waypoint -> Location.builder()
                .name(waypoint.name)
                .x(waypoint.longitude)
                .y(waypoint.latitude)
                .build())
            .toList();

        return WaypointsDirectionsRequest.builder()
            .origin(origin)
            .destination(destination)
            .waypoints(waypoints)
            .priority("RECOMMEND")
            .carFuel("GASOLINE")
            .carHipass(false)
            .alternatives(false)
            .roadDetails(true)
            .summary(false)
            .roadevent(1)
            .build();
    }

}
