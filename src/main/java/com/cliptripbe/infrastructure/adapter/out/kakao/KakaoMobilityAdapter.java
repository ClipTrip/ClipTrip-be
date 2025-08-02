package com.cliptripbe.infrastructure.adapter.out.kakao;


import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;
import com.cliptripbe.infrastructure.port.kakao.KakaoMobilityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMobilityAdapter implements KakaoMobilityPort {

    @Qualifier("kakaoMobilityRestClient")
    private final RestClient kakaoMobilityRestClient;

    @Override
    public WaypointsDirectionsResponse getDirections(WaypointsDirectionsRequest request) {
        long start = System.currentTimeMillis();

        WaypointsDirectionsResponse response = kakaoMobilityRestClient.post()
            .uri("/v1/waypoints/directions")
            .body(request)
            .retrieve()
            .toEntity(WaypointsDirectionsResponse.class)
            .getBody();

        long elapsed = System.currentTimeMillis() - start;
        log.info("카카오 모빌리티 길찾기 호출 레이턴시: {} ms", elapsed);

        return response;
    }
}

