package com.cliptripbe.feature.schedule.dto.response;

import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleResponse(
    Long id,
    String name,
    String description,
    List<PlaceListResponse> placeList

) {

    public static ScheduleResponse of(
        Schedule schedule,
        List<PlaceListResponse> placeListResponses
    ) {
        return ScheduleResponse.builder()
            .id(schedule.getId())
            .name(schedule.getName())
            .description(schedule.getDescription())
            .placeList(
                placeListResponses
            )
            .build();
    }
}
