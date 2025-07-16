package com.cliptripbe.feature.video.api.dto.response;

import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.application.SchedulePlaceMapper;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.Video;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record VideoScheduleResponse(
    VideoResponseDto videoResponse,
    ScheduleInfoResponseDto scheduleInfoResponse
) {

    public static VideoScheduleResponse of(Video video, Schedule schedule, Language language) {
        List<PlaceListResponseDto> placeListResponseDtos = schedule.getSchedulePlaceList().stream()
            .map(sp -> {
                Place place = sp.getPlace();
                Integer placeOrder = sp.getPlaceOrder();
                PlaceTranslation translation = place.getTranslationByLanguage(language);
                return PlaceListResponseDto.of(place, translation, placeOrder);
            })
            .toList();

        return VideoScheduleResponse.builder()
            .videoResponse(VideoResponseDto.from(video))
            .scheduleInfoResponse(
                SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule, placeListResponseDtos))
            .build();
    }
}
