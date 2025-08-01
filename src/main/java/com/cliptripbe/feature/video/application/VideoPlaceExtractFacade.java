package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.application.PlaceTranslationService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.infrastructure.adapter.out.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.port.kakao.KakaoMapPort;
import com.cliptripbe.global.util.prompt.PromptUtils;
import com.cliptripbe.global.util.prompt.type.PromptType;
import com.cliptripbe.infrastructure.port.caption.CaptionPort;
import com.cliptripbe.infrastructure.port.openai.ChatGptPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoPlaceExtractFacade {

    private final PlaceService placeService;
    private final ScheduleService scheduleService;
    private final VideoService videoService;
    private final PlaceTranslationService placeTranslationService;

    private final CaptionPort captionPort;
    private final ChatGptPort chatGptPort;
    private final KakaoMapPort kakaoMapPort;

    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequest request) {
        CaptionResponse caption = captionPort.getCaptions(request.youtubeUrl());

        String requestPlacePrompt = PromptUtils.build(PromptType.PLACE, caption.captions());

        String requestSummaryPrompt = PromptUtils.build(PromptType.SUMMARY_KO,
            caption.captions());

        String gptPlaceResponse = chatGptPort.askPlaceExtraction(requestPlacePrompt);
        List<String> extractPlacesText = ChatGPTUtils.extractPlaces(gptPlaceResponse);

        // 자막 요약
        String gptSummaryResponse = chatGptPort.ask(requestSummaryPrompt);
        String summaryKo = ChatGPTUtils.removeLiteralNewlines(gptSummaryResponse);

        // 자막 요약 영어
        String summaryTranslated = null;
        if (user.getLanguage() == Language.ENGLISH) {
            String requestSummaryEnPrompt = PromptUtils.build(PromptType.SUMMARY_EN,
                caption.captions());

            summaryTranslated = chatGptPort.ask(requestSummaryEnPrompt);
        }

        List<PlaceDto> places = kakaoMapPort.searchFirstPlacesInParallel(extractPlacesText);

        List<Place> placeList = placeService.createPlaceAll(places);
        if (user.getLanguage() == Language.ENGLISH) {
            placeList.forEach(placeTranslationService::registerPlace);
        }

        Video video = videoService.createVideo(request.toVideo(summaryKo, summaryTranslated));
        Schedule schedule = scheduleService.createScheduleByVideo(user, placeList);

        return VideoScheduleResponse.of(video, schedule, user.getLanguage());
    }
}
