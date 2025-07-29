package com.cliptripbe.feature.place.application;

import static com.cliptripbe.feature.user.domain.type.Language.ENGLISH;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.infrastructure.openai.prompt.type.PromptConstants;
import com.cliptripbe.infrastructure.openai.service.ChatGPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTranslationService {

    private final ChatGPTService chatGPTService;
    private final PlaceTranslationRepository placeTranslationRepository;

    @Transactional
    public void registerPlace(Place place) {
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
        String response = chatGPTService.ask(prompt);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, TranslationInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("번역 결과 파싱 실패", e);
        }
    }
}
