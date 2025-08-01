package com.cliptripbe.infrastructure.adapter.out.kakao;

import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.KakaoMapResponse;
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
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoMapAsyncAdapter {
    
    @Qualifier("kakaoRestClient")
    private final RestClient kakaoRestClient;
    
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
