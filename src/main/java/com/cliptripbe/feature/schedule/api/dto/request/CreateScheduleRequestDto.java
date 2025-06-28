package com.cliptripbe.feature.schedule.api.dto.request;

import com.cliptripbe.feature.place.domain.vo.PlaceVO;
import java.util.List;

public record CreateScheduleRequestDto(
    String scheduleName,
    String description,
    List<PlaceVO> placeVOList
) {

}
