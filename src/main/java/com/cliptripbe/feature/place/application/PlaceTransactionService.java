package com.cliptripbe.feature.place.application;

import static com.cliptripbe.global.response.type.ErrorType.FAIL_CREATE_PLACE_ENTITY;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.global.response.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceTransactionService {

    private final PlaceRepository placeRepository;
    private final PlaceTranslationService placeTranslationService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Place handleDuplicateKakaoPlaceId(String kakaoPlaceId) {
        // TODO : 여기서도 번역??????? 담당해야됨 리트라이하는 부분임
        Place place = placeRepository.findByKakaoPlaceId(kakaoPlaceId)
            .orElseThrow(() -> new CustomException(FAIL_CREATE_PLACE_ENTITY));
        placeTranslationService.registerPlace(place);
        return place;
    }
}
