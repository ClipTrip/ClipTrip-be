package com.cliptripbe.feature.translate.application;

import com.cliptripbe.feature.place.application.PlaceCacheService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.feature.translate.dto.response.TranslationSplitResult;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTranslationService {


    private final PlaceTranslationRepository placeTranslationRepository;
    private final PlaceCacheService placeCacheService;
    private final PlaceTranslator placeTranslator;

    @Transactional
    public void translateAndRegisterPlace(Place place, Language language) {
        //**TODO 여러 장소를 받자.
        placeTranslationRepository.findByPlaceAndLanguage(place, language)
            .orElseGet(() -> {
                TranslationInfo translationInfo = placeCacheService.getTranslationInfo(place, language)
                    .orElseGet(() -> placeTranslator.translatePlaceInfo(place, language));
                PlaceTranslation translation = PlaceTranslation.of(place, translationInfo, language);
                place.addTranslation(translation);
                return placeTranslationRepository.saveAndFlush(translation);
            });
    }

    public List<TranslatedPlaceAddress> getTranslatedPlaces(
        Language userLanguage,
        List<PlaceDto> placeDtoList
    ) {
        TranslationSplitResult result = placeCacheService.classifyPlaces(placeDtoList, userLanguage);

        List<TranslatedPlaceAddress> newTranslations = placeTranslator.translateList(
            result.untranslatedPlaces(), userLanguage
        );

        placeCacheService.cachePlace(newTranslations, userLanguage);

        return result.mergeWith(newTranslations);
    }
}
