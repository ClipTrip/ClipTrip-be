package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.domain.vo.PlaceDetailVO;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import java.util.List;

public class SchedulePlaceMapper {

    public static ScheduleInfoResponseDto map(Schedule schedule) {
        List<PlaceDetailVO> placeDetails = schedule.getSchedulePlaceList().stream()
            .map(SchedulePlace::toVO)
            .toList();
        return new ScheduleInfoResponseDto(schedule.getId(), schedule.getName(), placeDetails);
    }
}
