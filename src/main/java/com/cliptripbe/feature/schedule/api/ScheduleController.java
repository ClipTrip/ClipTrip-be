package com.cliptripbe.feature.schedule.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_VERSION + "/schedules")
@RequiredArgsConstructor
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

//    @Override
//    @PostMapping()
//    public ApiResponse<?> createScheduleByVideo(
//        @AuthenticationPrincipal CustomerDetails customerDetails,
//        @RequestBody CreateScheduleRequestDto createScheduleRequestDto) {
//        scheduleService.create(customerDetails.getUser(), createScheduleRequestDto);
//        return ApiResponse.success(SuccessType.CREATED);
//    }

    @Override
    @PostMapping()
    public ApiResponse<?> createSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        scheduleService.create(customerDetails.getUser());
        return ApiResponse.success(SuccessType.CREATED);
    }


    @Override
    @PutMapping("/{scheduleId}")
    public ApiResponse<?> updateSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable Long scheduleId,
        @RequestBody UpdateScheduleRequestDto updateSchedule
    ) {
        scheduleService.updateSchedule(customerDetails.getUser(), scheduleId, updateSchedule);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

//    @Override
//    @PostMapping("/{scheduleId}")
//    public ApiResponse<?> addPlaceInSchedule(
//        @AuthenticationPrincipal CustomerDetails customerDetails,
//        @PathVariable Long scheduleId,
//        @RequestBody PlaceInfoRequestDto placeInfoRequestDto
//    ) {
//        scheduleService.addPlaceInSchedule(
//            customerDetails.getUser(),
//            scheduleId,
//            placeInfoRequestDto
//        );
//        return ApiResponse.success(SuccessType.SUCCESS);
//    }
//
//    @Override
//    @DeleteMapping("/{scheduleId}")
//    public ApiResponse<?> deleteSchedulePlace(
//        @AuthenticationPrincipal CustomerDetails customerDetails,
//        @PathVariable Long scheduleId,
//        @RequestBody DeleteSchedulePlaceRequestDto deleteSchedulePlaceRequestDto
//    ) {
//        scheduleService.deleteSchedulePlace(
//            scheduleId,
//            deleteSchedulePlaceRequestDto.placeId(),
//            customerDetails.getUser());
//        return ApiResponse.success(SuccessType.SUCCESS);
//    }

    @Override
    @GetMapping()
    public ApiResponse<?> getUserSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<ScheduleListResponseDto> list = scheduleService.getUserSchedule(
            customerDetails.getUser());
        return ApiResponse.success(SuccessType.SUCCESS, list);
    }

    @Override
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<?> deleteUserSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable(value = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(customerDetails.getUser(), scheduleId);
        return ApiResponse.success(SuccessType.SUCCESS, scheduleId);
    }
}
