package com.cliptripbe.feature.schedule.api;

import com.cliptripbe.feature.schedule.dto.request.UpdateScheduleRequest;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "스케줄 관련 API, 로그인 필요")
public interface ScheduleControllerDocs {

    @Operation(summary = "새로운 일정 만들기, 빈 일정입니다.")
    ApiResponse<?> createSchedule(CustomerDetails customerDetails);

    @Operation(summary = "일정 수정하기, put 메서드")
    ApiResponse<Long> updateSchedule(
        CustomerDetails customerDetails,
        Long scheduleId,
        UpdateScheduleRequest updateSchedule
    );

    @Operation(summary = "유저 전체 일정 조회하기")
    ApiResponse<List<ScheduleListResponse>> getUserScheduleList(CustomerDetails customerDetails);

    @Operation(summary = "유저 일정 상제 조회하기")
    ApiResponse<ScheduleResponse> getUserSchedule(CustomerDetails customerDetails, Long scheduleId);

    @Operation(summary = "유저 일정 삭제하기")
    ApiResponse<Long> deleteUserSchedule(
        CustomerDetails customerDetails,
        Long scheduleId
    );
}
