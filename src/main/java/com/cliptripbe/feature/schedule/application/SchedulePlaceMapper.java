package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import java.util.List;

public class SchedulePlaceMapper {
    public static ScheduleResponse mapScheduleInfoResponseDto(
        Schedule schedule,
        List<PlaceListResponseDto> placeListResponseDtos
    ) {
        return ScheduleResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .placeList(placeListResponseDtos)
            .build();
    }

    public static ScheduleListResponseDto mapScheduleListResponseDto(Schedule schedule) {
        return ScheduleListResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .build();
    }
}
