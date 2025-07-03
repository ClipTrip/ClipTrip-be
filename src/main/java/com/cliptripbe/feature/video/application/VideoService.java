package com.cliptripbe.feature.video.application;

import static com.cliptripbe.global.response.type.ErrorType.CHATGPT_NO_RESPONSE;
import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.application.PlaceRegister;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.feature.video.infrastructure.VideoRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.caption.dto.CaptionRequest;
import com.cliptripbe.infrastructure.caption.dto.CaptionResponse;
import com.cliptripbe.infrastructure.caption.service.CaptionService;
import com.cliptripbe.infrastructure.caption.utils.CaptionUtils;
import com.cliptripbe.infrastructure.kakao.service.KakaoMapService;
import com.cliptripbe.infrastructure.openai.service.ChatGPTService;
import com.cliptripbe.infrastructure.openai.prompt.PromptConstants;
import com.cliptripbe.infrastructure.openai.utils.ChatGPTUtils;
import java.util.List;
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

    private final PlaceRegister placeRegister;
    private final VideoRepository videoRepository;

    private final ChatGPTService chatGPTService;
    private final KakaoMapService kakaoMapService;
    private final CaptionService captionService;

    public void extractPlace(User user, ExtractPlaceRequestDto request) {
        CaptionUtils.extractVideoId(request.youtubeUrl());
        CaptionResponse caption = captionService.getCaptions(
            CaptionRequest.of(request.youtubeUrl())
        );

        String requestPlacePrompt =
            PromptConstants.EXTRACT_CAPTION + System.lineSeparator() + caption.captions();

        String requestSummaryPrompt =
            PromptConstants.SUMMARY_CAPTION + System.lineSeparator() + caption.captions();

        List<String> extractPlacesText = chatGPTService.ask(requestPlacePrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .map(ChatGPTUtils::extractPlaces)
            .blockOptional()
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        String summaryKo = chatGPTService.ask(requestSummaryPrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .map(ChatGPTUtils::removeLiteralNewlines)
            .blockOptional()
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        List<PlaceDto> places = kakaoMapService.searchFirstPlaces(extractPlacesText)
            .subscribeOn(Schedulers.boundedElastic())
            .blockOptional()
            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));

        System.out.println("하이");
        placeRegister.registerAllPlaces(places);
        videoRepository.save(request.toVideo(summaryKo));
    }
}