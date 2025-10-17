package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoRegistrationService {

    private final PlaceService placeService;
    private final ScheduleService scheduleService;
    private final VideoService videoService;

    @Transactional
    public VideoScheduleResponse saveVideoAndSchedule(
        User user,
        Video videoToSave,
        List<PlaceDto> placeDtoList
    ) {
        Video video = videoService.createVideo(videoToSave);
        List<Place> placeEntityList = placeService.createPlaceAll(placeDtoList);
        ScheduleResponse scheduleResponse = scheduleService.createScheduleByVideo(user, placeEntityList);

        return VideoScheduleResponse.of(video, scheduleResponse);
    }
}
