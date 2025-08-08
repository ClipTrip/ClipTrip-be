package com.cliptripbe.feature.place.application;

import static com.cliptripbe.feature.user.domain.type.Language.ENGLISH;
import static com.cliptripbe.global.util.prompt.type.PromptConstants.TRANSLATE_PLACE_INFO_BATCH_PROMPT;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.domain.vo.TranslationInfoWithId;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.PlacePromptInput;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.prompt.type.PromptConstants;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTranslationService {

    private final ChatGptAdapter chatGptAdapter;
    private final PlaceTranslationRepository placeTranslationRepository;
    private final ObjectMapper objectMapper;
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

    private TranslationInfo translatePlaceInfo(Place place, Language language) {
        String prompt = PromptConstants.TRANSLATE_PLACE_INFO.formatted(
            language.getName(),
            place.getName(),
            place.getAddress().roadAddress()
        );
        String response = chatGptAdapter.ask(prompt);
        try {
            return objectMapper.readValue(response, TranslationInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("번역 결과 파싱 실패", e);
        }
    }

    public Map<String, TranslationInfoWithId> translatePlaceListBatch(
        List<PlaceDto> places,
        Language targetLanguage
    ) {
        int threadCount = Math.min(maxThreads, Math.max(1, places.size() / batchSize));
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<List<TranslationInfoWithId>>> futures = new ArrayList<>();

        for (int start = 0; start < places.size(); start += batchSize) {
            int end = Math.min(start + batchSize, places.size());
            List<PlacePromptInput> promptInputs = buildPromptInputs(places, start, end);
            futures.add(executor.submit(() ->
                buildTranslationTask(promptInputs, targetLanguage)
            ));
        }
        Map<String, TranslationInfoWithId> resultMap = collectTranslationResults(futures);
        executor.shutdown();
        return resultMap;
    }

    private List<TranslationInfoWithId> buildTranslationTask(
        List<PlacePromptInput> promptInputs,
        Language targetLanguage
    ) {
        String inputJson = toJson(promptInputs);
        String prompt = TRANSLATE_PLACE_INFO_BATCH_PROMPT.formatted(
            targetLanguage.getName(), inputJson
        );
        String responseJson = chatGptAdapter.ask(prompt);
        return parseTranslationResponse(responseJson);
    }

    private List<PlacePromptInput> buildPromptInputs(List<PlaceDto> places, int start, int end) {
        List<PlacePromptInput> inputs = new ArrayList<>();
        for (int i = start; i < end; i++) {
            PlaceDto p = places.get(i);
            inputs.add(new PlacePromptInput(String.valueOf(i), p.placeName(), p.roadAddress()));
        }
        return inputs;
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }

    private List<TranslationInfoWithId> parseTranslationResponse(String responseJson) {
        try {
            return objectMapper.readValue(responseJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답 JSON 파싱 실패: " + responseJson, e);
        }
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
            } catch (Exception e) {
                throw new RuntimeException("GPT 응답 병렬 처리 실패", e);
            }
        }
        return resultMap;
    }
}
