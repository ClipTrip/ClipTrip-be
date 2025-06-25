package com.cliptripbe.feature.schedule.api;

import com.cliptripbe.feature.schedule.api.dto.request.CreateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.request.DeleteSchedulePlaceRequestDto;
import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "스케줄 관련 API")
public interface ScheduleControllerDocs {

    @Operation(summary = "일정만들기, \n로그인 필요")
    ApiResponse<?> createSchedule(CustomerDetails customerDetails,
        CreateScheduleRequestDto createScheduleRequestDto);

    @Operation(summary = "일정 수정하기, \n로그인 필요,put 메서드")
    ApiResponse<?> updateSchedule(
        CustomerDetails customerDetails,
        Long scheduleId,
        UpdateScheduleRequestDto updateSchedule);

    @Operation(summary = "일정안에 있는 장소 삭제, \n로그인 필요,지울 장소는 리스트로 전달하여 복수 삭제 가능")
    ApiResponse<?> deleteSchedulePlace(
        CustomerDetails customerDetails,
        Long scheduleId,
        DeleteSchedulePlaceRequestDto deleteSchedulePlaceRequestDto
    );

    @Operation(summary = "유저 일정 조회하기, \n로그인 필요")
    ApiResponse<?> getUserSchedule(CustomerDetails customerDetails);
}
