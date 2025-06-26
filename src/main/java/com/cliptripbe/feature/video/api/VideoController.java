package com.cliptripbe.feature.video.api;

import com.cliptripbe.feature.video.application.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private VideoService videoService;



}
