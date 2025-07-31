package com.cliptripbe.feature.place.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.vo.LuggageStorageRequestDto;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.feature.place.dto.response.PlaceAccessibilityInfoResponse;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceResponse;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import com.cliptripbe.infrastructure.adapter.out.google.GooglePlacesAdapter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/places")
public class PlaceController implements PlaceControllerDocs {

    private final PlaceService placeService;
    private final GooglePlacesAdapter googlePlacesAdapter;

    @GetMapping("/accessInfo")
    public ApiResponse<?> getPlaceAccessibilityInfo(
        @ModelAttribute PlaceInfoRequest placeInfoRequest
    ) {
        PlaceAccessibilityInfoResponse placeAccessibilityInfo = placeService.getPlaceAccessibilityInfo(
            placeInfoRequest);
        return ApiResponse.success(SuccessType.OK, placeAccessibilityInfo);
    }

    @GetMapping
    public ApiResponse<?> getPlaceById(
        @ModelAttribute PlaceInfoRequest placeInfoRequest,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        PlaceAccessibilityInfoResponse placeAccessibilityInfo = placeService.getPlaceInfo(
            placeInfoRequest, customerDetails.getUser());
        return ApiResponse.success(SuccessType.OK, placeAccessibilityInfo);
    }

    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponse> getPlaceById(
        @PathVariable(value = "placeId") Long placeId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        PlaceResponse place = placeService.getPlaceById(placeId, customerDetails.getUser());
        return ApiResponse.success(SuccessType.OK, place);
    }

    @GetMapping("/category")
    public ApiResponse<List<PlaceListResponse>> getPlacesByCategory(
        @ModelAttribute @Valid PlaceSearchByCategoryRequest request
    ) {
        List<PlaceListResponse> places = placeService.getPlacesByCategory(request);
        return ApiResponse.success(SuccessType.OK, places);
    }

    @GetMapping("/keyword")
    public ApiResponse<List<PlaceListResponse>> getPlacesByKeyword(
        @ModelAttribute @Valid PlaceSearchByKeywordRequest request) {
        List<PlaceListResponse> places = placeService.getPlacesByKeyword(request);
        return ApiResponse.success(SuccessType.OK, places);
    }

    @Override
    @GetMapping("/luggage-storages")
    public ApiResponse<List<PlaceListResponse>> getLuggageStorages(
        @ModelAttribute @Valid LuggageStorageRequestDto luggageStorageRequestDto
    ) {
        List<PlaceListResponse> places = placeService.getLuggageStorage(
            luggageStorageRequestDto);
        return ApiResponse.success(SuccessType.OK, places);
    }
}
