package com.cliptripbe.feature.schedule.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.dto.request.UpdateScheduleRequest;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
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

    @Override
    @PostMapping
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
        @RequestBody UpdateScheduleRequest updateSchedule
    ) {
        scheduleService.updateSchedule(customerDetails.getUser(), scheduleId, updateSchedule);
        return ApiResponse.success(SuccessType.OK);
    }

    @Override
    @GetMapping()
    public ApiResponse<?> getUserScheduleList(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<ScheduleListResponse> list = scheduleService.getUserScheduleList(
            customerDetails.getUser());
        return ApiResponse.success(SuccessType.OK, list);
    }

    @Override
    @GetMapping("/{scheduleId}")
    public ApiResponse<?> getUserSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable(value = "scheduleId") Long scheduleId
    ) {
        ScheduleResponse scheduleResponse = scheduleService.getScheduleById(
            customerDetails.getUser(),
            scheduleId);
        return ApiResponse.success(SuccessType.OK, scheduleResponse);
    }

    @Override
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<?> deleteUserSchedule(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable(value = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(customerDetails.getUser(), scheduleId);
        return ApiResponse.success(SuccessType.OK, scheduleId);
    }
}
