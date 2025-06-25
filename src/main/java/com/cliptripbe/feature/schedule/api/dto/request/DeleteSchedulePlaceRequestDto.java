package com.cliptripbe.feature.schedule.api.dto.request;

import java.util.List;

public record DeleteSchedulePlaceRequestDto(
    List<Long> placeIdList
) {

}
