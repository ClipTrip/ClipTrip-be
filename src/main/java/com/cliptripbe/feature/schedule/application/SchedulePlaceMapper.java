package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import java.util.List;

public class SchedulePlaceMapper {

    public static ScheduleResponse mapScheduleInfoResponseDto(
        Schedule schedule,
        List<PlaceListResponse> placeListResponses
    ) {
        return ScheduleResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .placeList(placeListResponses)
            .build();
    }

    public static ScheduleListResponse mapScheduleListResponseDto(Schedule schedule) {
        return ScheduleListResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .build();
    }
}
