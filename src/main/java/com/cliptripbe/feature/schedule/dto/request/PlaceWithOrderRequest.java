package com.cliptripbe.feature.schedule.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import lombok.Builder;

@Builder
public record PlaceWithOrderRequest(
    Integer placeOrder,
    PlaceInfoRequest placeInfo
) {

}
