package com.cliptripbe.feature.schedule.api;

import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.global.auth.security.CustomerDetails;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "스케줄 관련 API, 로그인 필요")
public interface ScheduleControllerDocs {

//    @Operation(summary = "일정만들기, (장소 리스트를 받으면서 만들기)")
//    ApiResponse<?> createScheduleByVideo(CustomerDetails customerDetails,
//        CreateScheduleRequestDto createScheduleRequestDto);

    @Operation(summary = "새로운 일정 만들기, 빈 일정입니다.")
    ApiResponse<?> createSchedule(CustomerDetails customerDetails);

    @Operation(summary = "일정 수정하기,put 메서드")
    ApiResponse<?> updateSchedule(
        CustomerDetails customerDetails,
        Long scheduleId,
        UpdateScheduleRequestDto updateSchedule
    );

//    @Operation(summary = "일정안에 있는 장소 삭제, 지울 장소는 리스트로 ")
//    ApiResponse<?> deleteSchedulePlace(
//        CustomerDetails customerDetails,
//        Long scheduleId,
//        DeleteSchedulePlaceRequestDto deleteSchedulePlaceRequestDto
//    );

    @Operation(summary = "유저 전체 일정 조회하기")
    ApiResponse<?> getUserScheduleList(CustomerDetails customerDetails);

    @Operation(summary = "유저 일정 상제 조회하기")
    ApiResponse<?> getUserSchedule(Long scheduleId);

    @Operation(summary = "유저 일정 삭제하기")
    ApiResponse<?> deleteUserSchedule(
        CustomerDetails customerDetails,
        Long scheduleId
    );

//    @Operation(summary = "유저 일정안에 장소 추가하기")
//    ApiResponse<?> addPlaceInSchedule(
//        CustomerDetails customerDetails,
//        Long scheduleId,
//        PlaceInfoRequestDto placeInfoRequestDto
//    );
}
