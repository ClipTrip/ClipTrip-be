package com.cliptripbe.feature.video.application;

import static com.cliptripbe.global.response.type.ErrorType.FAIL_ASYNC_WORK;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.global.util.prompt.PromptUtils;
import com.cliptripbe.global.util.prompt.type.PromptType;
import com.cliptripbe.global.util.prompt.type.SummaryType;
import com.cliptripbe.infrastructure.adapter.out.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.port.caption.VideoContentExtractPort;
import com.cliptripbe.infrastructure.port.kakao.PlaceSearchPort;
import com.cliptripbe.infrastructure.port.openai.AiTextProcessorPort;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoPlaceExtractFacade {

    private final VideoContentExtractPort videoContentExtractPort;
    private final AiTextProcessorPort aiTextProcessorPort;
    private final PlaceSearchPort placeSearchPort;

    private final VideoAsyncProcessor videoAsyncProcessor;
    private final VideoRegistrationService videoRegistrationService;

    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequest request) {
        // 1
        CaptionResponse caption = videoContentExtractPort.getCaptions(request.youtubeUrl());

        String requestPlacePrompt = PromptUtils.build(PromptType.PLACE, caption.captions());
        String requestSummaryPrompt = PromptUtils.build(SummaryType.KOREAN, caption.captions());

        CompletableFuture<List<PlaceDto>> placeFuture =
            videoAsyncProcessor.extractAndSearchPlaces(requestPlacePrompt);

        CompletableFuture<String> summaryFuture =
            videoAsyncProcessor.generateVideoSummary(requestSummaryPrompt);

        try {
            CompletableFuture.allOf(placeFuture, summaryFuture).join();

            String summaryKo = summaryFuture.join();
            List<PlaceDto> placeDtoList = placeFuture.join();

            Video videoToSave = request.toVideo(summaryKo);

            return videoRegistrationService.saveVideoAndSchedule(user, videoToSave, placeDtoList);
        } catch (CompletionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof CustomException ce) {
                throw ce;
            }
            log.error("비동기 작업 실패", e);
            throw new CustomException(FAIL_ASYNC_WORK);
        }

//        // 2
//        String gptPlaceResponse = aiTextProcessorPort.askPlaceExtraction(requestPlacePrompt);
//        List<String> extractPlacesText = ChatGPTUtils.extractPlaces(gptPlaceResponse);
//
//        // 3
//        String gptSummaryResponse = aiTextProcessorPort.ask(requestSummaryPrompt);
//        String summaryKo = ChatGPTUtils.removeLiteralNewlines(gptSummaryResponse);
//
//        Video videoToSave = request.toVideo(summaryKo);
//        // 2
//        List<PlaceDto> placeDtoLst = placeSearchPort.searchFirstPlacesInParallel(extractPlacesText);
//
//        return videoRegistrationService.saveVideoAndSchedule(user, videoToSave, placeDtoLst);
    }
}
