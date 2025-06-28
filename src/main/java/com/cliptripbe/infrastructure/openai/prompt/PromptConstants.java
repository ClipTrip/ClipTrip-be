package com.cliptripbe.infrastructure.openai.prompt;

public class PromptConstants {

    public static final String EXTRACT_CAPTION =
        "해당 자막에서 ‘장소’, ‘식당’, ‘카페’ 등으로 추정되는 단어만 모두 골라주세요. " +
            "응답은 오직 장소 이름만, 줄바꿈으로 구분하여 나열해주세요. " +
            "설명 문장이나 머리말(예: “다음과 같습니다”)은 절대 포함하지 마세요.";

}
