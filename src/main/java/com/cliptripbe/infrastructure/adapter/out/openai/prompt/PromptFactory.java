package com.cliptripbe.infrastructure.adapter.out.openai.prompt;

import com.cliptripbe.infrastructure.adapter.out.openai.prompt.type.PromptType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptFactory {

    public String build(PromptType type, String captions) {
        return type.getTemplate() + System.lineSeparator() + captions;
    }

}
