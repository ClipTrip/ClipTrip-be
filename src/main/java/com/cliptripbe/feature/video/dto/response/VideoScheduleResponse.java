package com.cliptripbe.feature.video.dto.response;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.application.SchedulePlaceMapper;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.video.domain.Video;
import java.util.List;
import lombok.Builder;

@Builder
public record VideoScheduleResponse(
    VideoResponse videoResponse,
    ScheduleInfoResponseDto scheduleInfoResponse
) {

    public static VideoScheduleResponse of(Video video, Schedule schedule,
        List<PlaceListResponseDto> placeListResponseDtos) {

        return VideoScheduleResponse.builder()
            .videoResponse(VideoResponse.from(video))
            .scheduleInfoResponse(
                SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule, placeListResponseDtos))
            .build();
    }
}
