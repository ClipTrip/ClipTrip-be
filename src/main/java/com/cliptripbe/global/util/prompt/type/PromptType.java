package com.cliptripbe.global.util.prompt.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PromptType {
    PLACE(PromptConstants.EXTRACT_PLACE),
    SUMMARY_KO(PromptConstants.SUMMARY_CAPTION),
    SUMMARY_EN(PromptConstants.SUMMARY_CAPTION_EN);

    private final String template;
}
