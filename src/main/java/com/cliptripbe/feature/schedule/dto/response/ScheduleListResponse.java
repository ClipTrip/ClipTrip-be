package com.cliptripbe.feature.schedule.dto.response;

import lombok.Builder;

@Builder
public record ScheduleListResponse(
    Long scheduleId,
    String scheduleName,
    String description
) {

}
