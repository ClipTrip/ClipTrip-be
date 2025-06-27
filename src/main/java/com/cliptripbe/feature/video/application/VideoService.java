package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.infrastructure.openai.application.ChatGPTService;
import com.cliptripbe.infrastructure.openai.enums.PromptEnum;
import com.cliptripbe.infrastructure.openai.utils.ChatGptUtils;
import com.cliptripbe.infrastructure.youtube.YoutubeService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final ChatGPTService chatGPTService;

    public void extractPlace(User user, ExtractPlaceRequestDto request) {
        String caption = "전체 자막";

        String requestPrompt =
            caption + System.lineSeparator() + PromptEnum.EXTRACT_CAPTION.getPrompt();

        String rawResponse = chatGPTService.ask(requestPrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .block();

        if (rawResponse != null) {
            List<String> extractPlaces = ChatGptUtils.extractPlaces(rawResponse);
        }


    }
}