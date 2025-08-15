package com.cliptripbe.feature.video.dto.response;

import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.video.domain.entity.Video;
import lombok.Builder;

@Builder
public record VideoScheduleResponse(
    VideoResponse videoResponse,
    ScheduleResponse scheduleInfoResponse
) {

    public static VideoScheduleResponse of(Video video, ScheduleResponse schedule) {
        return VideoScheduleResponse.builder()
            .videoResponse(VideoResponse.from(video))
            .scheduleInfoResponse(schedule)
            .build();
    }
}
