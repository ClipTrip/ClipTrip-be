package com.cliptripbe.feature.schedule.api.dto.response;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.vo.PlaceDetailVO;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleInfoResponseDto(
    Long scheduleId,
    String scheduleName,
    String description,
    List<PlaceListResponseDto> placeList
) {

}
