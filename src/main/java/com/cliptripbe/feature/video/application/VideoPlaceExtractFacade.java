package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.translate.application.PlaceTranslationService;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.global.util.prompt.PromptUtils;
import com.cliptripbe.global.util.prompt.type.PromptType;
import com.cliptripbe.infrastructure.adapter.out.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.port.caption.VideoContentExtractPort;
import com.cliptripbe.infrastructure.port.kakao.PlaceSearchPort;
import com.cliptripbe.infrastructure.port.openai.AiTextProcessorPort;
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

    private final VideoContentExtractPort videoContentExtractPort;
    private final AiTextProcessorPort aiTextProcessorPort;
    private final PlaceSearchPort placeSearchPort;

    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequest request) {
        CaptionResponse caption = videoContentExtractPort.getCaptions(request.youtubeUrl());

        String requestPlacePrompt = PromptUtils.build(PromptType.PLACE, caption.captions());
        String requestSummaryPrompt = PromptUtils.build(PromptType.SUMMARY_KO, caption.captions());

        String gptPlaceResponse = aiTextProcessorPort.askPlaceExtraction(requestPlacePrompt);
        List<String> extractPlacesText = ChatGPTUtils.extractPlaces(gptPlaceResponse);

        String gptSummaryResponse = aiTextProcessorPort.ask(requestSummaryPrompt);
        String summaryKo = ChatGPTUtils.removeLiteralNewlines(gptSummaryResponse);

        String summaryTranslated = null;
        if (user.getLanguage() == Language.ENGLISH) {
            String requestSummaryEnPrompt = PromptUtils.build(PromptType.SUMMARY_EN,
                caption.captions());

            summaryTranslated = aiTextProcessorPort.ask(requestSummaryEnPrompt);
        }

        List<PlaceDto> places = placeSearchPort.searchFirstPlacesInParallel(extractPlacesText);

        List<Place> placeList = placeService.createPlaceAll(places);
        if (user.getLanguage() == Language.ENGLISH) {
            placeList.forEach(placeTranslationService::registerPlace);
        }

        Video video = videoService.createVideo(request.toVideo(summaryKo, summaryTranslated));
        Schedule schedule = scheduleService.createScheduleByVideo(user, placeList);

        return VideoScheduleResponse.of(video, schedule, user.getLanguage());
    }
}
