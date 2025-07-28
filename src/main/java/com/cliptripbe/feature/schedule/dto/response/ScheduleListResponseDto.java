package com.cliptripbe.feature.schedule.dto.response;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import lombok.Builder;

@Builder
public record ScheduleListResponseDto(
    Long scheduleId,
    String scheduleName,
    String description
) {

    public static ScheduleListResponseDto fromSchedule(Schedule schedule) {
        return ScheduleListResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .build();
    }
}
