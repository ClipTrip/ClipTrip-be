package com.cliptripbe.infrastructure.kakao.service;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByCategoryRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByKeywordRequestDto;
import com.cliptripbe.infrastructure.kakao.dto.KakaoMapResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMapService {

    @Qualifier("kakaoRestClient")
    private final RestClient kakaoRestClient;

    public List<PlaceDto> searchPlacesByCategory(PlaceSearchByCategoryRequestDto req) {
        long start = System.currentTimeMillis();

        KakaoMapResponse resp = kakaoRestClient.get()
            .uri(uri -> uri
                .path("/v2/local/search/category.json")
                .queryParam("category_group_code", req.categoryCode())
                .queryParam("x", req.x())
                .queryParam("y", req.y())
                .queryParam("radius", req.radius())
                .build()
            )
            .retrieve()
            .body(KakaoMapResponse.class);

        List<PlaceDto> places = Optional.ofNullable(resp)
            .map(KakaoMapResponse::documents)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(PlaceDto::from)
            .toList();

        long elapsed = System.currentTimeMillis() - start;
        log.info("카테고리 호출 레이턴시: {} ms", elapsed);
        return places;
    }

    public List<PlaceDto> searchPlaces(PlaceSearchByKeywordRequestDto req) {
        long start = System.currentTimeMillis();

        KakaoMapResponse resp = kakaoRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", req.query())
                .queryParam("x", req.x())
                .queryParam("y", req.y())
                .queryParam("radius", req.radius())
                .build()
            )
            .retrieve()
            .body(KakaoMapResponse.class);

        List<PlaceDto> places = Optional.ofNullable(resp)
            .map(KakaoMapResponse::documents)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(PlaceDto::from)
            .toList();

        long elapsed = System.currentTimeMillis() - start;
        log.info("[{}] 개별 호출 레이턴시: {} ms", req.query(), elapsed);

        return places;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<PlaceDto> searchFirstPlaceAsync(String keyword) {
        long start = System.currentTimeMillis();

        KakaoMapResponse resp = kakaoRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .build())
            .retrieve()
            .body(KakaoMapResponse.class);

        PlaceDto place = Optional.ofNullable(resp)
            .map(KakaoMapResponse::documents)
            .orElseGet(Collections::emptyList).stream()
            .map(PlaceDto::from)
            .findFirst()
            .orElse(null);

        log.info("[{}] 개별 호출 레이턴시: {} ms", keyword, System.currentTimeMillis() - start);
        return CompletableFuture.completedFuture(place);
    }

    public List<PlaceDto> searchFirstPlacesAsync(List<String> keywords) {
        List<CompletableFuture<PlaceDto>> futures = keywords.stream()
            .map(this::searchFirstPlaceAsync)
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
            .map(CompletableFuture::join)
            .toList();
    }
}
