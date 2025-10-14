package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.Map;

public class ScheduleResponseAssembler {

    public static ScheduleResponse createScheduleResponseForKorean(
        Schedule schedule,
        Map<Long, List<Long>> bookmarkIdsMap,
        User user
    ) {
        List<PlaceListResponse> placeListResponses = schedule.getPlaces().stream()
            .map(place -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(place.getId(), List.of());
                return PlaceListResponse.ofEntity(place, user.getLanguage(), bookmarkIds);
            })
            .toList();
        return ScheduleResponse.of(schedule, placeListResponses);
    }
}
