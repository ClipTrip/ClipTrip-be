package com.cliptripbe.feature.schedule.dto.request;

import java.util.List;

public record UpdateScheduleRequest(
    String scheduleName,
    String description,
    List<PlaceWithOrderRequest> placeInfoRequests
) {

}
