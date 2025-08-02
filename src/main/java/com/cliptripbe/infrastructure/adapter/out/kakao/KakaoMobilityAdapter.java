package com.cliptripbe.infrastructure.adapter.out.kakao;


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
}

