package com.cliptripbe.global.util.prompt.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PromptType {
    PLACE(PromptConstants.EXTRACT_PLACE);

    private final String template;
}
