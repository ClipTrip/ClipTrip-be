package com.cliptripbe.feature.video.application;

import static com.cliptripbe.global.response.type.ErrorType.CHATGPT_NO_RESPONSE;
import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.application.PlaceRegister;
import com.cliptripbe.feature.place.application.PlaceTranslationService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.schedule.application.ScheduleRegister;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.domain.impl.ScheduleFinder;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.feature.video.api.dto.response.VideoScheduleResponse;
import com.cliptripbe.feature.video.domain.Video;
import com.cliptripbe.feature.video.infrastructure.VideoRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.caption.dto.CaptionRequest;
import com.cliptripbe.infrastructure.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.caption.service.CaptionService;
import com.cliptripbe.infrastructure.caption.utils.CaptionUtils;
import com.cliptripbe.infrastructure.kakao.service.KakaoMapService;
import com.cliptripbe.infrastructure.openai.service.ChatGPTService;
import com.cliptripbe.infrastructure.openai.prompt.PromptConstants;
import com.cliptripbe.global.util.ChatGPTUtils;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final ScheduleRegister scheduleRegister;
    private final ScheduleFinder scheduleFinder;
    private final PlaceRegister placeRegister;
    private final VideoRepository videoRepository;
    private final PlaceTranslationService placeTranslationService;

    private final ChatGPTService chatGPTService;
    private final KakaoMapService kakaoMapService;
    private final CaptionService captionService;

    /**
     * Processes a YouTube video URL to extract mentioned places from captions, generate summaries, register related places and schedules, and return a combined video and schedule response.
     *
     * The method uses AI services to extract place names and generate summaries from video captions, searches for place details, registers places (and their translations if the user language is English), creates a video and schedule entity, associates places with the schedule, and returns a response containing the video and schedule information.
     *
     * @param user the user requesting the extraction, whose language preference affects summary and translation handling
     * @param request the request containing the YouTube video URL and related data
     * @return a response containing the saved video, the associated schedule, and language-specific information
     * @throws CustomException if external AI or map services fail to respond
     */
    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequestDto request) {
        CaptionUtils.extractVideoId(request.youtubeUrl());
        CaptionResponse caption = captionService.getCaptions(
            CaptionRequest.of(request.youtubeUrl())
        );

        String requestPlacePrompt =
            PromptConstants.EXTRACT_CAPTION + System.lineSeparator() + caption.captions();

        String requestSummaryPrompt =
            PromptConstants.SUMMARY_CAPTION + System.lineSeparator() + caption.captions();

        String requestSummaryEnPrompt =
            PromptConstants.SUMMARY_CAPTION_EN + System.lineSeparator() + caption.captions();

        List<String> extractPlacesText = chatGPTService.askPlaceExtraction(requestPlacePrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .map(ChatGPTUtils::extractPlaces)
            .blockOptional()
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        String summaryKo = chatGPTService.ask(requestSummaryPrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .map(ChatGPTUtils::removeLiteralNewlines)
            .blockOptional()
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        String summaryTranslated = null;
        if (user.getLanguage() == Language.ENGLISH) {
            summaryTranslated = chatGPTService.ask(requestSummaryEnPrompt)
                .subscribeOn(Schedulers.boundedElastic())
                .map(ChatGPTUtils::removeLiteralNewlines)
                .blockOptional()
                .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));
        }

        List<PlaceDto> places = kakaoMapService.searchFirstPlaces(extractPlacesText)
            .subscribeOn(Schedulers.boundedElastic())
            .blockOptional()
            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));

        List<Place> placeEntities = placeRegister.registerAllPlaces(places);
        if (user.getLanguage() == Language.ENGLISH) {
            placeEntities.forEach(placeTranslationService::registerPlace);
        }
        Video video = videoRepository.save(request.toVideo(summaryKo, summaryTranslated));
        Schedule schedule = scheduleRegister.registerSchedule(user);

        // 스케줄 서비스로 뺴기
        IntStream.range(0, placeEntities.size())
            .mapToObj(i -> SchedulePlace.builder()
                .place(placeEntities.get(i))
                .schedule(schedule)
                .placeOrder(i)
                .build()
            )
            .forEach(schedule::addSchedulePlace);

//        Schedule scheduleEntity = scheduleFinder.findByIdWithSchedulePlacesAndTranslations(
//            schedule.getId(),
//            user.getLanguage()
//        );

        Schedule scheduleEntity = scheduleFinder.getScheduleById(schedule.getId());
        return VideoScheduleResponse.of(video, scheduleEntity, user.getLanguage());
    }
}