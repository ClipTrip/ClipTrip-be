package com.cliptripbe.feature.schedule.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import java.util.List;

public record CreateScheduleRequest(
    String scheduleName,
    String description,
    List<PlaceInfoRequest> placeInfoRequests
) {

}
