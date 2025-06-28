package com.cliptripbe.feature.schedule.api.dto.response;

import lombok.Builder;

@Builder
public record ScheduleListResponseDto(
    String scheduleName,
    String description
) {

}
