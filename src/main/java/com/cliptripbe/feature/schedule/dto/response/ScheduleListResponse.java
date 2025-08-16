package com.cliptripbe.feature.schedule.dto.response;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import lombok.Builder;

@Builder
public record ScheduleListResponse(
    Long scheduleId,
    String scheduleName,
    String description,
    Integer schedulePlaceCount
) {

    public static ScheduleListResponse from(Schedule schedule) {
        return ScheduleListResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .schedulePlaceCount(schedule.getSchedulePlaces().size())
            .build();
    }
}
