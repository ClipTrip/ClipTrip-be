package com.cliptripbe.global.util.prompt;

import com.cliptripbe.global.util.prompt.type.PromptType;
import com.cliptripbe.global.util.prompt.type.SummaryType;

public class PromptUtils {

    public static String build(PromptType type, String captions) {
        return type.getTemplate() + System.lineSeparator() + captions;
    }

    public static String build(SummaryType summaryType, String captions) {
        return summaryType.getTemplate() + System.lineSeparator() + captions;
    }

}
