package com.cliptripbe.feature.schedule.api.dto.request;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import java.util.List;

public record UpdateScheduleRequestDto(
    String scheduleName,
    List<PlaceInfoRequestDto> placeInfoRequestDtos
) {

}
