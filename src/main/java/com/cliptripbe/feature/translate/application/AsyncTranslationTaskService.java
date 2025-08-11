package com.cliptripbe.feature.translate.application;

import static com.cliptripbe.global.util.ChatGPTUtils.buildPromptInputs;

import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.PlacePromptInput;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncTranslationTaskService {

    private final static Integer BATCH_SIZE = 5;
    private final AsyncHelper asyncHelper;

    public List<TranslationInfo> translate(List<PlaceDto> placeDtos, Language userLanguage) {
        List<CompletableFuture<List<TranslationInfo>>> futures = new ArrayList<>();
        for (int start = 0; start < placeDtos.size(); start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, placeDtos.size());
            List<PlacePromptInput> promptInputs = buildPromptInputs(placeDtos, start, end);
            futures.add(
                asyncHelper.asyncTranslateTask(promptInputs, userLanguage));
        }
        return collectTranslationResults(futures);
    }

    protected List<TranslationInfo> collectTranslationResults(
        List<CompletableFuture<List<TranslationInfo>>> futures
    ) {
        List<TranslationInfo> translationInfoList = new ArrayList<>();
        for (CompletableFuture<List<TranslationInfo>> future : futures) {
            try {
                translationInfoList.addAll(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CustomException(ErrorType.INTERRUPT_TRANSLATE);
            } catch (ExecutionException e) {
                throw new CustomException(ErrorType.FAIL_GPT_TRANSLATE);
            }
        }
        return translationInfoList;
    }
}
