package com.cliptripbe.infrastructure.google.service;

import static com.cliptripbe.global.response.type.ErrorType.GOOGLE_PLACES_EMPTY_RESPONSE;

import com.cliptripbe.global.config.WebClientConfig;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.google.dto.GooglePlacesSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GooglePlacesService {

    @Qualifier("googleWebClient")
    private final WebClient googleWebClient;
    private final WebClientConfig webClientConfig;


    public Mono<byte[]> getPhotoByAddress(String address) {
        // 1) Text Search API 호출
        return googleWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/textsearch/json")
                .queryParam("query", address)
                .queryParam("key", webClientConfig.getGoogleApiKey())
                .build()
            )
            .retrieve()
            .bodyToMono(GooglePlacesSearchResponse.class)
            .flatMap(resp -> {
                var results = resp.results();
                if (results == null || results.isEmpty()) {
                    return Mono.error(new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));
                }
                var photos = results.get(0).photos();
                if (photos == null || photos.isEmpty()) {
                    return Mono.error(new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));
                }
                String photoRef = photos.get(0).photoReference();
                return fetchPhotoByReference(photoRef);
            });
    }

    private Mono<byte[]> fetchPhotoByReference(String photoReference) {
        return googleWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/photo")
                .queryParam("photoreference", photoReference)
                .queryParam("maxwidth", "360")
                .queryParam("key", webClientConfig.getGoogleApiKey())
                .build()
            )
            .exchangeToMono(resp -> {
                if (resp.statusCode().is3xxRedirection()) {
                    String location = resp.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
                    return googleWebClient.get()
                        .uri(location)
                        .retrieve()
                        .bodyToMono(byte[].class);
                } else {
                    return resp.createException().flatMap(Mono::error);
                }
            });
    }
}
