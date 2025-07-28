package com.cliptripbe.feature.schedule.dto.response;

import com.cliptripbe.feature.place.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleResponse(
    Long scheduleId,
    String scheduleName,
    String description,
    List<PlaceListResponseDto> placeList
) {

    public static ScheduleResponse of(Schedule schedule, Language language) {
        return ScheduleResponse.builder()
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
}
