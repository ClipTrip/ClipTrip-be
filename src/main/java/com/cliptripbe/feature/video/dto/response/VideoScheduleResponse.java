package com.cliptripbe.feature.video.dto.response;

import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.schedule.application.SchedulePlaceMapper;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import lombok.Builder;

@Builder
public record VideoScheduleResponse(
    VideoResponse videoResponse,
    ScheduleResponse scheduleInfoResponse
) {

    public static VideoScheduleResponse of(Video video, Schedule schedule, Language language) {
        return VideoScheduleResponse.builder()
            .videoResponse(VideoResponse.from(video))
            .scheduleInfoResponse(ScheduleResponse.of(schedule, language))
            .build();
    }
}
