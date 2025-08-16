package com.cliptripbe.feature.translate.application;


import com.cliptripbe.feature.translate.dto.request.PlacePromptInput;
import com.cliptripbe.feature.translate.dto.request.TranslationInfoWithIndex;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.global.util.prompt.type.LanguagePromptType;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AsyncHelper {

    /*
     * @Async 메서드 호출을 위한 헬퍼 컴포넌트.
     *
     * Spring의 AOP 프록시 기반 @Async는 동일 클래스 내의 메서드 호출에서는 동작하지 않습니다.
     * 따라서 비동기 처리가 필요한 로직을 별도의 컴포넌트로 분리하여,
     * 다른 서비스 레이어에서 이 클래스의 메서드를 호출하도록 설계했습니다.
     * 'threadPoolTaskExecutor' 스레드 풀을 사용하여 ChatGpt API 호출과 같은
     * 오래 걸리는 작업을 메인 스레드와 분리해 애플리케이션의 응답성을 향상시킵니다.
     */

    private final JsonUtils jsonUtils;
    private final ChatGptAdapter chatGptAdapter;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<TranslationInfoWithIndex>> asyncTranslateTask(
        List<PlacePromptInput> promptInputs,
        Language targetLanguage
    ) {
        String inputJson = jsonUtils.toJson(promptInputs);
        LanguagePromptType languagePromptType = LanguagePromptType.findByLanguage(targetLanguage);
        String prompt = languagePromptType.getBatchPrompt().formatted(inputJson);
        String responseJson = chatGptAdapter.ask(prompt);
        List<TranslationInfoWithIndex> translationInfoWithIndices = jsonUtils.parseToList(responseJson,
            TranslationInfoWithIndex.class);
        return CompletableFuture.completedFuture(translationInfoWithIndices);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<TranslationInfoDto> asyncTranslateSinglePlace(String prompt) {
        try {
            String response = chatGptAdapter.ask(prompt);
            TranslationInfoDto translationInfo = jsonUtils.readValue(response, TranslationInfoDto.class);
            return CompletableFuture.completedFuture(translationInfo);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}