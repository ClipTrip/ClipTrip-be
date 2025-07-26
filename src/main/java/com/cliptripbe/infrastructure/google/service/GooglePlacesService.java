package com.cliptripbe.infrastructure.google.service;

import static com.cliptripbe.global.response.type.ErrorType.GOOGLE_PLACES_EMPTY_RESPONSE;

import com.cliptripbe.global.config.RestClientConfig;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.google.dto.GooglePlacesSearchResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GooglePlacesService {

    @Qualifier("googleMapsRestClient")
    private final RestClient googleMapsRestClient;
    private final RestClientConfig restClientConfig;


    public byte[] getPhotoByAddress(String address) {
        // 1) Text Search API 호출
        GooglePlacesSearchResponse searchResp = googleMapsRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/textsearch/json")
                .queryParam("query", address)
                .queryParam("key", restClientConfig.getGoogleApiKey())
                .build()
            )
            .retrieve()
            .body(GooglePlacesSearchResponse.class);

        List<GooglePlacesSearchResponse.TextSearchResult> results = Optional.ofNullable(searchResp)
            .map(GooglePlacesSearchResponse::results)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));

        GooglePlacesSearchResponse.PhotoDto photoDto = Optional.ofNullable(results.getFirst().photos())
            .filter(list -> !list.isEmpty())
            .map(List::getFirst)
            .orElseThrow(() -> new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));

        String photoReference = photoDto.photoReference();
        byte[] photoBytes = fetchPhotoByReference(photoReference);

        return Optional.ofNullable(photoBytes)
            .filter(bytes -> bytes.length > 0)
            .orElseThrow(() -> new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));
    }

    private byte[] fetchPhotoByReference(String photoReference) {
        ResponseEntity<Void> initial = googleMapsRestClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/photo")
                .queryParam("photoreference", photoReference)
                .queryParam("maxwidth", "360")
                .queryParam("key", restClientConfig.getGoogleApiKey())
                .build()
            )
            .retrieve()
            .toBodilessEntity();

        if (initial.getStatusCode().is3xxRedirection()) {
            String redirectUrl = initial.getHeaders().getLocation().toString();
            ResponseEntity<byte[]> photoResp = googleMapsRestClient.get()
                .uri(redirectUrl)
                .retrieve()
                .toEntity(byte[].class);

            if (photoResp.getStatusCode().is2xxSuccessful() && photoResp.hasBody()) {
                return photoResp.getBody();
            }
        }
        throw new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE);
    }
}
