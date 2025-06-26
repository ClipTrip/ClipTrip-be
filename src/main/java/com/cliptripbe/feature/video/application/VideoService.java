package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.infrastructure.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final YoutubeService youtubeService;

    public void extractPlace(User user, ExtractPlaceRequestDto request) {
        Mono<String> caption = youtubeService.extractPlainCaptions(request.youtubeUrl());
        String captionText = caption.block();
        System.out.println("유튜브 자막: " + captionText);
    }
}
