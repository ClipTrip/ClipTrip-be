package com.cliptripbe.feature.video.api.dto.response;

import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.application.SchedulePlaceMapper;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.video.domain.Video;
import lombok.Builder;

@Builder
public record VideoScheduleResponse(
    VideoResponseDto videoResponse,
    ScheduleInfoResponseDto scheduleInfoResponse
) {

    public static VideoScheduleResponse of(Video video, Schedule schedule) {
        return VideoScheduleResponse.builder()
            .videoResponse(VideoResponseDto.from(video))
            .scheduleInfoResponse(SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule))
            .build();
    }
}
