package com.cliptripbe.feature.schedule.api.dto.request;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import java.util.List;

public record CreateScheduleRequestDto(
    String scheduleName,
    String description,
    List<PlaceInfoRequestDto> placeInfoRequestDtos
) {

}
