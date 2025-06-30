package com.cliptripbe.feature.place.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.application.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/places")
public class PlaceController implements PlaceControllerDocs {

    final PlaceService placeService;

    @GetMapping("/accessInfo")
    public PlaceAccessibilityInfoResponse getPlaceAccessibilityInfo(
        @ModelAttribute PlaceInfoRequestDto placeInfoRequestDto
    ) {
        PlaceAccessibilityInfoResponse accessibilityInfoResponses = placeService.getPlaceAccessibilityInfo(
            placeInfoRequestDto);
        return accessibilityInfoResponses;
    }
}
