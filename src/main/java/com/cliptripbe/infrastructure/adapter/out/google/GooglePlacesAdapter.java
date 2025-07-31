package com.cliptripbe.infrastructure.adapter.out.google;

import static com.cliptripbe.global.response.type.ErrorType.GOOGLE_PLACES_EMPTY_RESPONSE;

import com.cliptripbe.global.config.RestClientConfig;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.adapter.out.google.dto.GooglePlacesSearchResponse;
import com.cliptripbe.infrastructure.port.google.GooglePlacesPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GooglePlacesAdapter implements GooglePlacesPort {

    @Qualifier("googleMapsRestClient")
    private final RestClient googleMapsRestClient;
    private final RestClientConfig restClientConfig;

    public byte[] getPhotoByAddress(String address) {
        long start = System.currentTimeMillis();

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

        GooglePlacesSearchResponse.PhotoDto photoDto = Optional.ofNullable(
                results.getFirst().photos())
            .filter(list -> !list.isEmpty())
            .map(List::getFirst)
            .orElseThrow(() -> new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));

        String photoReference = photoDto.photoReference();
        byte[] photoBytes = fetchPhotoByReference(photoReference);
        byte[] responseBytes = Optional.ofNullable(photoBytes)
            .filter(bytes -> bytes.length > 0)
            .orElseThrow(() -> new CustomException(GOOGLE_PLACES_EMPTY_RESPONSE));

        long elapsed = System.currentTimeMillis() - start;
        log.info("구글맵 사진 추출 레이턴시: {} ms", elapsed);
        return responseBytes;
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
