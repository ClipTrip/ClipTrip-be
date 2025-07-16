package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import java.util.List;

public class SchedulePlaceMapper {

    /**
     * Converts a Schedule entity into a ScheduleInfoResponseDto, including its associated places.
     *
     * The returned DTO contains the schedule's ID, name, description, and a list of places with their order as defined in the schedule.
     *
     * @param schedule the Schedule entity to convert
     * @return a ScheduleInfoResponseDto representing the schedule and its ordered places
     */
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


    /**
     * Creates a {@link ScheduleInfoResponseDto} from a {@link Schedule} entity and a provided list of place response DTOs.
     *
     * @param schedule the schedule entity to convert
     * @param placeListResponseDtos the list of place response DTOs to include in the response
     * @return a {@link ScheduleInfoResponseDto} containing schedule details and the provided place list
     */
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

    /**
     * Converts a Schedule entity into a ScheduleListResponseDto containing its ID, name, and description.
     *
     * @param schedule the Schedule entity to convert
     * @return a ScheduleListResponseDto representing the schedule's basic information
     */
    public static ScheduleListResponseDto mapScheduleListResponseDto(Schedule schedule) {
        return ScheduleListResponseDto.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getName())
            .description(schedule.getDescription())
            .build();
    }
}
