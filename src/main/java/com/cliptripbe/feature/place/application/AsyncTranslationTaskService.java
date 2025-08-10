package com.cliptripbe.feature.place.application;

import static com.cliptripbe.global.util.prompt.type.PromptConstants.TRANSLATE_PLACE_INFO_BATCH_PROMPT;

import com.cliptripbe.feature.place.domain.vo.TranslationInfoWithId;
import com.cliptripbe.feature.place.dto.PlacePromptInput;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import java.util.List;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncTranslationTaskService {

    private final JsonUtils jsonUtils;
    private final ChatGptAdapter chatGptAdapter;

    @Async("threadPoolTaskExecutor")
    public Future<List<TranslationInfoWithId>> asyncTranslateTask(
        List<PlacePromptInput> promptInputs,
        Language targetLanguage
    ) {
        String inputJson = jsonUtils.toJson(promptInputs);
        String prompt = TRANSLATE_PLACE_INFO_BATCH_PROMPT.formatted(
            targetLanguage.getName(), inputJson
        );
        String responseJson = chatGptAdapter.ask(prompt);
        return new AsyncResult<>(jsonUtils.parseToList(responseJson, TranslationInfoWithId.class));
    }
}
