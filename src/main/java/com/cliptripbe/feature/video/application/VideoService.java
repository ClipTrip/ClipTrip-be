package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.infrastructure.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final YoutubeService youtubeService;

    public void extractPlace(User user, ExtractPlaceRequestDto request) {

    }
}