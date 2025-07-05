package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceResponseDto;
import com.cliptripbe.feature.place.domain.vo.PlaceDetailVO;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import java.util.List;
import java.util.stream.Collectors;

public class SchedulePlaceMapper {

    public static ScheduleInfoResponseDto mapScheduleInfoResponseDto(Schedule schedule) {
        return ScheduleInfoResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .placeList(
                schedule.getPlaces().stream()
                    .map(PlaceListResponseDto::fromEntity)
                    .toList()
            )
            .build();
    }

    public static ScheduleListResponseDto mapScheduleListResponseDto(Schedule schedule) {
        return ScheduleListResponseDto
            .builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .build();
    }
}
