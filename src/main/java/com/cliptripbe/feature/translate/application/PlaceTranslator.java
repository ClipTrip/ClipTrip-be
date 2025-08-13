package com.cliptripbe.feature.translate.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.translate.dto.request.PlacePromptInput;
import com.cliptripbe.feature.translate.dto.request.TranslationInfoWithIndex;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.global.util.prompt.type.PromptConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceTranslator {

    private final JsonUtils jsonUtils;
    private final static Integer BATCH_SIZE = 5;
    private final AsyncHelper asyncHelper;

    public List<TranslatedPlaceAddress> translateList(List<PlaceDto> placeDtos, Language userLanguage) {
        List<CompletableFuture<List<TranslationInfoWithIndex>>> futures = new ArrayList<>();
        for (int i = 0; i < placeDtos.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, placeDtos.size());
            List<PlacePromptInput> promptInputs = ChatGPTUtils.buildPromptInputs(placeDtos, i, end);
            futures.add(asyncHelper.asyncTranslateTask(promptInputs, userLanguage));
        }
        List<TranslationInfoWithIndex> translationInfoWithIndices = collectTranslatedPlaceDtos(futures);
        return getTranslatedPlaceAddressList(translationInfoWithIndices, placeDtos, userLanguage);
    }

    public TranslationInfoDto translatePlaceInfo(Place place, Language language) {
        if (place == null || place.getName() == null || place.getAddress() == null
            || place.getAddress().roadAddress() == null) {
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
        }
        String prompt = PromptConstants.TRANSLATE_PLACE_INFO.formatted(
            language.getName(),
            place.getName(),
            place.getAddress().roadAddress()
        );
        try {
            CompletableFuture<TranslationInfoDto> futureTranslation = asyncHelper.asyncTranslateSinglePlace(prompt);
            return futureTranslation.get();
        } catch (Exception e) {
            throw new CustomException(ErrorType.FAIL_GPT_TRANSLATE);
        }
    }

    private List<TranslatedPlaceAddress> getTranslatedPlaceAddressList(
        List<TranslationInfoWithIndex> translationInfoWithIndices, List<PlaceDto> placeDtos, Language userLanguage
    ) {
        return translationInfoWithIndices.stream()
            .map(translationInfoWithIndex -> {
                PlaceDto placeDto = placeDtos.get(translationInfoWithIndex.index());
                TranslationInfoDto translationInfoDto = TranslationInfoDto.from(translationInfoWithIndex);
                return TranslatedPlaceAddress.of(placeDto, translationInfoDto, userLanguage);
            })
            .toList();
    }

    protected List<TranslationInfoWithIndex> collectTranslatedPlaceDtos(
        List<CompletableFuture<List<TranslationInfoWithIndex>>> futures
    ) {
        List<TranslationInfoWithIndex> allTranslations = new ArrayList<>();

        for (CompletableFuture<List<TranslationInfoWithIndex>> future : futures) {
            try {
                List<TranslationInfoWithIndex> translationInfos = future.get();
                allTranslations.addAll(translationInfos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CustomException(ErrorType.INTERRUPT_TRANSLATE);
            } catch (ExecutionException e) {
                throw new CustomException(ErrorType.FAIL_GPT_TRANSLATE);
            }
        }
        return allTranslations;
    }
}