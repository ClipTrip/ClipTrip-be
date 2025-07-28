package com.cliptripbe.feature.schedule.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequestDto;
import java.util.List;

public record UpdateScheduleRequestDto(
    String scheduleName,
    String description,
    List<PlaceInfoRequestDto> placeInfoRequestDtos
) {

}
