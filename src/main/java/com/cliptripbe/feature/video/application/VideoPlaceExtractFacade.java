package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.application.PlaceTranslationService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.schedule.application.ScheduleService;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.dto.request.ExtractPlaceRequest;
import com.cliptripbe.feature.video.dto.response.VideoScheduleResponse;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.infrastructure.caption.dto.CaptionRequest;
import com.cliptripbe.infrastructure.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.caption.service.CaptionService;
import com.cliptripbe.infrastructure.kakao.service.KakaoMapService;
import com.cliptripbe.infrastructure.openai.prompt.PromptFactory;
import com.cliptripbe.infrastructure.openai.prompt.type.PromptType;
import com.cliptripbe.infrastructure.openai.service.ChatGPTService;
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

    private final CaptionService captionService;
    private final ChatGPTService chatGPTService;
    private final KakaoMapService kakaoMapService;
    private final PromptFactory promptFactory;

    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequest request) {
        // 1. url로 자막을 추출한다 - 외부 api(fastAPI)
        // 2-1. 추출된 자막을 통해 장소를 요약한다 - 외부 api(gpt)
        // 2-2. 추출된 자막을 통해 장소를 추출한다 - 외부 api(gpt)
        // 3. 추출된 장소를 통해 실제 장소 정보를 가져온다 - 외부 api(kakao)
        // 4. video, place, schedule 엔티티를 생성 후 저장한다.
        // 5. 추출된 엔티티를 바탕으로 사용자에게 응답해준다.

        CaptionResponse caption = captionService.getCaptions(
            CaptionRequest.of(request.youtubeUrl())
        );

        String requestPlacePrompt = promptFactory.build(PromptType.PLACE, caption.captions());

        String requestSummaryPrompt = promptFactory.build(PromptType.SUMMARY_KO,
            caption.captions());

        String gptPlaceResponse = chatGPTService.askPlaceExtraction(requestPlacePrompt);
        List<String> extractPlacesText = ChatGPTUtils.extractPlaces(gptPlaceResponse);

        // 자막 요약
        String gptSummaryResponse = chatGPTService.ask(requestSummaryPrompt);
        String summaryKo = ChatGPTUtils.removeLiteralNewlines(gptSummaryResponse);

        // 자막 요약 영어
        String summaryTranslated = null;
        if (user.getLanguage() == Language.ENGLISH) {
            String requestSummaryEnPrompt = promptFactory.build(PromptType.SUMMARY_EN,
                caption.captions());

            summaryTranslated = chatGPTService.ask(requestSummaryEnPrompt);
        }

        List<PlaceDto> places = kakaoMapService.searchFirstPlacesAsync(extractPlacesText);

        List<Place> placeList = placeService.createPlaceAll(places);
        if (user.getLanguage() == Language.ENGLISH) {
            placeList.forEach(placeTranslationService::registerPlace);
        }

        Video video = videoService.createVideo(request.toVideo(summaryKo, summaryTranslated));
        Schedule schedule = scheduleService.createScheduleByVideo(user, placeList);

        return VideoScheduleResponse.of(video, schedule, user.getLanguage());
    }
}
