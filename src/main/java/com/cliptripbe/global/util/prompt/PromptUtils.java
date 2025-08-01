package com.cliptripbe.global.util.prompt;

import com.cliptripbe.global.util.prompt.type.PromptType;

public class PromptUtils {

    public static String build(PromptType type, String captions) {
        return type.getTemplate() + System.lineSeparator() + captions;
    }

}
