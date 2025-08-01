package com.cliptripbe.infrastructure.port.caption;

import com.cliptripbe.infrastructure.adapter.out.caption.dto.CaptionResponse;

public interface CaptionPort {

    CaptionResponse getCaptions(String youtubeUrl);

}
