package com.cliptripbe.feature.place.api;

import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Place 관련 API")
public interface PlaceControllerDocs {

    @Operation(summary = "장소이름을 받고 그 장소 정보 조회 API")
    PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(String placeName);
}
