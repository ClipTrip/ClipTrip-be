package com.cliptripbe.feature.place.api;

import static com.cliptripbe.global.config.Constant.API_VERSION;

import com.cliptripbe.feature.place.api.dto.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.application.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/places")
public class PlaceController implements PlaceControllerDocs {

    final PlaceService placeService;

    @GetMapping("/{name}")
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        @PathVariable(value = "name") String placeName
    ) {
        PlaceAccessibilityInfoResponse accessibilityInfoResponses = placeService.getPlaceAccessibilityInfo(
            placeName);
        return accessibilityInfoResponses;
    }
}
