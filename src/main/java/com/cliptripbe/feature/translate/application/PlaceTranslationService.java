package com.cliptripbe.feature.translate.application;

import com.cliptripbe.feature.place.application.PlaceCacheService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.infrastructure.PlaceTranslationRepository;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.translate.dto.response.TranslationSplitResult;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                TranslationInfoDto translationInfoDto = placeCacheService.getTranslationInfo(place, language)
                    .orElseGet(() -> placeTranslator.translatePlaceInfo(place, language));
                PlaceTranslation translation = PlaceTranslation.of(place, translationInfoDto, language);
                place.addTranslation(translation);
                return placeTranslationRepository.saveAndFlush(translation);
            });
    }

    @Transactional
    public void translateAndRegisterPlaces(List<PlaceDto> places, List<Place> placeEntityList,
        Language language) {

        TranslationSplitResult result = placeCacheService.classifyPlaces(places, language);

        List<TranslatedPlaceAddress> newTranslations = placeTranslator.translateList(
            result.untranslatedPlaces(), language
        );

        List<TranslatedPlaceAddress> translatedPlaceAddresses = result.mergeWith(newTranslations);

        Map<String, Place> placeMap = placeEntityList.stream()
            .collect(Collectors.toMap(
                Place::getTranslationKey, p -> p,
                (prev, curr) -> prev
            ));

        List<PlaceTranslation> placeTranslations = translatedPlaceAddresses.stream()
            .map(translated -> {
                Place originalPlace = placeMap.get(translated.getTranslationKey());
                if (originalPlace == null) {
                    throw new CustomException(ErrorType.PLACE_NOT_FOUND);
                }

                return PlaceTranslation.builder()
                    .place(originalPlace)
                    .language(language)
                    .name(translated.translationInfoDto().translatedName())
                    .roadAddress(translated.translationInfoDto().translatedRoadAddress())
                    .build();
            })
            .collect(Collectors.toList());

        placeTranslationRepository.saveAll(placeTranslations);
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

    public List<PlaceTranslation> findByPlaceIdInAndLanguage(List<Long> placeIds, Language language) {
        return placeTranslationRepository.findByPlaceIdInAndLanguage(placeIds, language);
    }
}
