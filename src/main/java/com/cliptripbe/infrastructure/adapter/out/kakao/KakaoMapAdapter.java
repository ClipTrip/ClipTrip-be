package com.cliptripbe.infrastructure.adapter.out.kakao;

import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.KakaoMapResponse;
import com.cliptripbe.infrastructure.port.kakao.KakaoMapPort;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMapAdapter implements KakaoMapPort {

    @Qualifier("kakaoRestClient")
    private final RestClient kakaoRestClient;

    @Override
    public List<PlaceDto> searchPlacesByCategory(PlaceSearchByCategoryRequest request) {
        log.debug("Searching places by category: {}", request.categoryCode());
        try {
            long start = System.currentTimeMillis();

            KakaoMapResponse response = kakaoRestClient.get()
                .uri(uri -> uri
                    .path("/v2/local/search/category.json")
                    .queryParam("category_group_code", request.categoryCode())
                    .queryParam("x", request.x())
                    .queryParam("y", request.y())
                    .queryParam("radius", request.radius())
                    .build()
                )
                .retrieve()
                .body(KakaoMapResponse.class);

            List<PlaceDto> places = Optional.ofNullable(response)
                .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE))
                .documents()
                .stream()
                .map(PlaceDto::from)
                .toList();

            long elapsed = System.currentTimeMillis() - start;
            log.info("카테고리 호출 레이턴시: {} ms", elapsed);

            return places;
        } catch (RestClientException e) {
            log.error("카카오 API 호출 실패: {}", e.getMessage());
            throw new CustomException(KAKAO_MAP_NO_RESPONSE);
        }
    }

    @Override
    public List<PlaceDto> searchPlacesByKeyWord(PlaceSearchByKeywordRequest request) {
        log.debug("Searching places by keyword: {}", request.query());
        try {
            long start = System.currentTimeMillis();

            KakaoMapResponse response = kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v2/local/search/keyword.json")
                    .queryParam("query", request.query())
                    .queryParam("x", request.x())
                    .queryParam("y", request.y())
                    .queryParam("radius", request.radius())
                    .build()
                )
                .retrieve()
                .body(KakaoMapResponse.class);

            List<PlaceDto> places = Optional.ofNullable(response)
                .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE))
                .documents()
                .stream()
                .map(PlaceDto::from)
                .toList();

            long elapsed = System.currentTimeMillis() - start;
            log.info("[{}] 개별 호출 레이턴시: {} ms", request.query(), elapsed);

            return places;

        } catch (RestClientException e) {
            log.error("카카오 API 호출 실패: {}", e.getMessage());
            throw new CustomException(KAKAO_MAP_NO_RESPONSE);
        }
    }

    @Override
    public List<PlaceDto> searchFirstPlacesInParallel(List<String> keywords) {
        log.debug("Searching first places async for keywords: {}", keywords);

        List<CompletableFuture<PlaceDto>> futures = keywords.stream()
            .map(this::searchFirstPlaceAsync)
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .toList();
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<PlaceDto> searchFirstPlaceAsync(String keyword) {
        try {
            long start = System.currentTimeMillis();

            KakaoMapResponse response = kakaoRestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v2/local/search/keyword.json")
                    .queryParam("query", keyword)
                    .build())
                .retrieve()
                .body(KakaoMapResponse.class);

            List<KakaoMapResponse.Document> documents = Optional.ofNullable(response)
                .map(KakaoMapResponse::documents)
                .orElseGet(Collections::emptyList);

            PlaceDto place = documents.stream()
                .map(PlaceDto::from)
                .findFirst()
                .orElse(null);

            log.info("[{}] 개별 호출 레이턴시: {} ms", keyword, System.currentTimeMillis() - start);
            return CompletableFuture.completedFuture(place);

        } catch (RestClientException e) {
            log.error("카카오 API 호출 실패: {}", e.getMessage());
            throw new CustomException(KAKAO_MAP_NO_RESPONSE);
        }
    }
}
