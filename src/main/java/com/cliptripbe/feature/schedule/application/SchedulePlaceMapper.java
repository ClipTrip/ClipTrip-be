package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import java.util.List;

public class SchedulePlaceMapper {

    public static ScheduleInfoResponseDto mapScheduleInfoResponseDto(Schedule schedule) {
        return ScheduleInfoResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .placeList(
                schedule.getSchedulePlaceList().stream()
                    .map(schedulePlace -> PlaceListResponseDto.fromEntity(
                        schedulePlace.getPlace(),
                        schedulePlace.getPlaceOrder())
                    )
                    .toList()
            )
            .build();
    }


    public static ScheduleInfoResponseDto mapScheduleInfoResponseDto(
        Schedule schedule,
        List<PlaceListResponseDto> placeListResponseDtos
    ) {
        return ScheduleInfoResponseDto.builder()
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
