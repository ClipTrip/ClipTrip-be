package com.cliptripbe.feature.video.application;

import com.cliptripbe.infrastructure.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final YoutubeService youtubeService;


}
