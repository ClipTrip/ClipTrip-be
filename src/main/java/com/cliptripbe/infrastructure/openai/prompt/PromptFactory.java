package com.cliptripbe.infrastructure.openai.prompt;

import com.cliptripbe.infrastructure.openai.prompt.type.PromptType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptFactory {

    public String build(PromptType type, String captions) {

        return null;
    }

}
