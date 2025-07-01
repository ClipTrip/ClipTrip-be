package com.cliptripbe.infrastructure.kakao;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByCategoryRequestDto;
import com.cliptripbe.feature.place.api.dto.request.PlaceSearchByKeywordRequestDto;
import com.cliptripbe.infrastructure.kakao.dto.KakaoMapResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    @Qualifier("kakaoWebClient")
    private final WebClient kakaoWebClient;

    public List<PlaceDto> searchPlacesByCategory(PlaceSearchByCategoryRequestDto req) {
        long start = System.currentTimeMillis();
        return kakaoWebClient.get()
            .uri(uri -> uri
                .path("/v2/local/search/category.json")
                .queryParam("category_group_code", req.categoryCode())
                .queryParam("x", req.x())
                .queryParam("y", req.y())
                .queryParam("radius", req.radius())
                .build()
            )
            .retrieve()
            .bodyToMono(KakaoMapResponse.class)
            .map(resp -> resp.documents().stream()
                .map(PlaceDto::from)
                .toList()
            )
            .doOnSuccess(place -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("카테고리 호출 레이턴시: {} ms", elapsed);
            })
            .block();

    }

    public List<PlaceDto> searchPlaces(PlaceSearchByKeywordRequestDto req) {
        long start = System.currentTimeMillis();
        return kakaoWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", req.query())
                .queryParam("x", req.x())
                .queryParam("y", req.y())
                .queryParam("radius", req.radius())
                .build()
            )
            .retrieve()
            .bodyToMono(KakaoMapResponse.class)
            .map(resp -> resp.documents().stream()
                .map(PlaceDto::from)
                .toList()
            )
            .doOnSuccess(place -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("[{}] 개별 호출 레이턴시: {} ms", req.query(), elapsed);
            })
            .block();
    }

    public Mono<PlaceDto> searchFirstPlace(String keyword) {
        long start = System.currentTimeMillis();
        return kakaoWebClient.get()
            .uri(u -> u.path("/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .build())
            .retrieve()
            .bodyToMono(KakaoMapResponse.class)

            .flatMapMany(resp -> Flux.fromIterable(
                resp.documents().stream()
                    .map(PlaceDto::from)
                    .toList()
            ))
            .next()
            .doOnSuccess(place -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("[{}] 개별 호출 레이턴시: {} ms", keyword, elapsed);
            });
    }

    public Mono<List<PlaceDto>> searchFirstPlaces(List<String> keywords) {
        long start = System.currentTimeMillis();

        return Flux.fromIterable(keywords)
            .flatMap(this::searchFirstPlace)
            .collectList()
            .doOnSuccess(result -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("kakaoMap 전체 성공 레이턴시: {} ms", elapsed);
            });
    }
}
