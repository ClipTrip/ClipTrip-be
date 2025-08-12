package com.cliptripbe.feature.translate.application;

import static com.cliptripbe.feature.user.domain.type.Language.ENGLISH;

import com.cliptripbe.feature.place.application.PlaceCacheService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.feature.translate.dto.response.TranslationSplitResult;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.JsonUtils;
import com.cliptripbe.global.util.prompt.type.PromptConstants;
import com.cliptripbe.infrastructure.adapter.out.openai.ChatGptAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTranslationService {

    private final ChatGptAdapter chatGptAdapter;
    private final PlaceTranslationRepository placeTranslationRepository;
    private final JsonUtils jsonUtils;
    private final PlaceCacheService placeCacheService;
    private final AsyncTranslationTaskService asyncTranslationTaskService;

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

    public List<TranslatedPlaceAddress> getTranslatedPlaces(
        Language userLanguage,
        List<PlaceDto> placeDtoList
    ) {
        TranslationSplitResult result = placeCacheService.classifyPlaces(placeDtoList, userLanguage);

        List<TranslatedPlaceAddress> newTranslations = asyncTranslationTaskService.translate(
            result.untranslatedPlaces(), userLanguage
        );

        placeCacheService.cachePlace(newTranslations, userLanguage);

        return result.mergeWith(newTranslations);
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
}
