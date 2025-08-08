package com.cliptripbe.feature.place.application;

import static com.cliptripbe.feature.user.domain.type.Language.ENGLISH;
import static com.cliptripbe.global.util.ChatGPTUtils.buildPromptInputs;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.TRANSLATE_PLACE_INFO_BATCH_PROMPT;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.domain.vo.TranslationInfoWithId;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.PlacePromptInput;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.global.util.prompt.type.PromptConstants;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTranslationService {

    private final ChatGptAdapter chatGptAdapter;
    private final PlaceTranslationRepository placeTranslationRepository;
    private final JsonUtils jsonUtils;

    private final int batchSize = 5;
    private final int maxThreads = 10;

    @Transactional
    public void registerPlace(Place place) {
        //**TODO 여러 장소를 받자.
        placeTranslationRepository.findByPlaceAndLanguage(place, ENGLISH)
            .orElseGet(() -> {
                TranslationInfo translationInfo = translatePlaceInfo(place, ENGLISH);
                PlaceTranslation translation = PlaceTranslation.builder()
                    .place(place)
                    .name(translationInfo.translatedName())
                    .roadAddress(translationInfo.translatedRoadAddress())
                    .language(ENGLISH)
                    .build();
                place.addTranslation(translation);
                return placeTranslationRepository.saveAndFlush(translation);
            });
    }

    @Transactional(readOnly = true)
    public Map<String, TranslationInfoWithId> getTranslatedPlacesMapIfRequired(
        Language userLanguage, List<PlaceDto> categoryPlaces
    ) {
        Map<String, TranslationInfoWithId> translatedPlacesMap = Collections.emptyMap();
        if (userLanguage != Language.KOREAN) {
            translatedPlacesMap = translatePlaceListBatch(
                categoryPlaces, userLanguage);
        }
        return translatedPlacesMap;
    }

    private TranslationInfo translatePlaceInfo(Place place, Language language) {
        String prompt = PromptConstants.TRANSLATE_PLACE_INFO.formatted(
            language.getName(),
            place.getName(),
            place.getAddress().roadAddress()
        );
        String response = chatGptAdapter.ask(prompt);

        return jsonUtils.readValue(response, TranslationInfo.class);

    }

    private Map<String, TranslationInfoWithId> translatePlaceListBatch(
        List<PlaceDto> places,
        Language targetLanguage
    ) {
        int threadCount = Math.min(maxThreads, Math.max(1, places.size() / batchSize));
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<List<TranslationInfoWithId>>> futures = new ArrayList<>();
            for (int start = 0; start < places.size(); start += batchSize) {
                int end = Math.min(start + batchSize, places.size());
                List<PlacePromptInput> promptInputs = buildPromptInputs(places, start, end);
                futures.add(executor.submit(() -> buildTranslationTask(promptInputs, targetLanguage)
                ));
            }
            Map<String, TranslationInfoWithId> resultMap = collectTranslationResults(futures);
            return resultMap;
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private List<TranslationInfoWithId> buildTranslationTask(
        List<PlacePromptInput> promptInputs,
        Language targetLanguage
    ) {
        String inputJson = jsonUtils.toJson(promptInputs);
        String prompt = TRANSLATE_PLACE_INFO_BATCH_PROMPT.formatted(
            targetLanguage.getName(), inputJson
        );
        String responseJson = chatGptAdapter.ask(prompt);
        return jsonUtils.parseToList(responseJson, TranslationInfoWithId.class);
    }

    private Map<String, TranslationInfoWithId> collectTranslationResults(
        List<Future<List<TranslationInfoWithId>>> futures
    ) {
        Map<String, TranslationInfoWithId> resultMap = new HashMap<>();
        for (Future<List<TranslationInfoWithId>> future : futures) {
            try {
                for (TranslationInfoWithId info : future.get()) {
                    resultMap.put(info.id(), info);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CustomException(ErrorType.INTERRUPT_TRANSLATE);
            } catch (ExecutionException e) {
                throw new CustomException(ErrorType.FAIL_GPT_TRANSLATE);
            }
        }
        return resultMap;
    }
}
