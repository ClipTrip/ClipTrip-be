package com.cliptripbe.feature.translate.application;

import static com.cliptripbe.global.util.prompt.type.PromptConstants.TRANSLATE_PLACE_INFO_BATCH_PROMPT;

import com.cliptripbe.feature.translate.dto.request.PlacePromptInput;
import com.cliptripbe.feature.translate.dto.request.TranslationInfoWithIndex;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// 새로운 비동기 작업을 담당하는 서비스
@Component
@RequiredArgsConstructor
public class AsyncHelper {

    private final JsonUtils jsonUtils;
    private final ChatGptAdapter chatGptAdapter;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<TranslationInfoWithIndex>> asyncTranslateTask(
        List<PlacePromptInput> promptInputs,
        Language targetLanguage
    ) {
        String inputJson = jsonUtils.toJson(promptInputs);
        String prompt = TRANSLATE_PLACE_INFO_BATCH_PROMPT.formatted(
            targetLanguage.getName(), inputJson
        );
        String responseJson = chatGptAdapter.ask(prompt);
        List<TranslationInfoWithIndex> translationInfoWithIndices = jsonUtils.parseToList(responseJson,
            TranslationInfoWithIndex.class);
        return CompletableFuture.completedFuture(translationInfoWithIndices);
    }
}