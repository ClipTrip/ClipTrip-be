package com.cliptripbe.global.youtube;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    @Qualifier("youtubeWebClient")
    private final WebClient youtubeWebClient;


}
