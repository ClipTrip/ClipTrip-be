package com.cliptripbe.global.util.prompt.type;

public class PromptConstants {

    public static final String EXTRACT_PLACE =
        "너는 아래 여행 자막 전체에서 등장하는 모든 장소(POI)를 추출해 Kakao Local Keyword API용 키워드를 만들어야 해. \n"
            + "한 장소만이 아니라, 자막 안에 나온 모든 장소를 빠짐없이 찾아야 해. 중복 장소는 한 번만, 다른 장소는 빠뜨리지 마.\n"
            + "출력은 반드시 규칙에 따라 정제된 장소명만 줄바꿈으로 나열해야 해.\n"
            + "\n"
            + "[장소로 인정하는 패턴]\n"
            + "- 고유명+업종 (예: 미르분식, 미르 분식)\n"
            + "- 고유명(2자 이상) + ‘집’, ‘식당’, ‘카페’, ‘베이커리’ 등의 접미\n"
            + "- 체인 브랜드 단독 (예: 스타벅스, 다이소) → 직전 context(동)를 상속\n"
            + "- 상호명 없이 음식 이름만 등장"
            + "한 경우 → 직전 context(시·군·구 또는 동) + 음식명\n"
            + "\n"
            + "[제외 조건]\n"
            + "- 업종명만 있는 경우 (‘카페’, ‘식당’ 등)이고 글자 수가 2자 이하인 경우 → 제외\n"
            + "- 업종명만 있어도 앞뒤 자막에 ‘먹다’, ‘가다’, ‘맛있다’ 등의 동사가 있으면 포함\n"
            + "\n"
            + "[지명/출력 규칙]\n"
            + "- 동명 장소 방지를 위해 meta location을 활용하고, context(행정구 또는 동)를 반드시 채워야 해.\n"
            + "\n"
            + "출력 형식:  \n"
            + "- 기본: 시·군·구 + 장소명\n"
            + "- 체인 또는 다지점: 시·군·구·동 + 장소명\n"
            + "- 응답은 오직 장소 이름만, 줄바꿈으로 구분하여 나열해. \n"
            + "- 괄호도 제외해.\n"
            + "- 설명 문장이나 머리말(예: “~다음과 같습니다”)은 절대 제외해. \n"
            + "- 예시 출력(정확히 이 형식으로):\n"
            + "경주 황리단길\n"
            + "서울 광화문\n"
            + "경주 불국사\n"
            + "경주 퍼즈 하우스\n";
    ;

    public static final String SUMMARY_CAPTION =
        "너는 영상 자막을 아주 쉬운 한국어로 요약하는 AI야.\n"
            + "이 요약의 목적은 문해력이 낮은 사람과 외국인이 장소별 활동을 쉽게 이해하고, 여행 일정을 만들 수 있도록 돕는 거야.\n"
            + "[요약 규칙]\n"
            + "1. 장소별 활동을 장소 등장 순서대로 요약해.\n"
            + "2. 장소명은 꼭 포함하고, 거기서 무엇을 했는지 간단히 말해.\n"
            + "3. 한 문장엔 하나의 정보만 담고, 40자 이내로 써.\n"
            + "4. 한자어, 외래어, 줄임말 등 어려운 말은 쉬운 우리말로 바꾸거나 괄호로 설명한다.\n"
            + "5. 평가 없이 사실만 써.\n"
            + "6. 중복된 정보는 제거하고 중요한 장소 및 활동만 담아.\n"
            + "7. 전체 요약은 500자 이내의 짧은 문장 여러 개로 구성해.\n"
            + "8. 장소명 앞에 지명(시·군·구·동)을 쓸 때는 다음 규칙을 따라.\n"
            + "    1. 시·군·구가 바뀌면 반드시 포함한다.\n"
            + "    2. 같은 시·군·구 내에서는 동과 장소명만 쓴다.\n";

    public static final String SUMMARY_CAPTION_EN =
        "너는 영상 자막을 아주 쉬운 한국어로 요약하는 AI야.\n"
            + "이 요약의 목적은 문해력이 낮은 사람과 외국인이 장소별 활동을 쉽게 이해하고, 여행 일정을 만들 수 있도록 돕는 거야.\n"
            + "[요약 규칙]\n"
            + "1. 장소별 활동을 장소 등장 순서대로 요약해.\n"
            + "2. 장소명은 꼭 포함하고, 거기서 무엇을 했는지 간단히 말해.\n"
            + "3. 한 문장엔 하나의 정보만 담고, 40자 이내로 써.\n"
            + "4. 한자어, 외래어, 줄임말 등 어려운 말은 쉬운 우리말로 바꾸거나 괄호로 설명한다.\n"
            + "5. 평가 없이 사실만 써.\n"
            + "6. 중복된 정보는 제거하고 중요한 장소 및 활동만 담아.\n"
            + "7. 전체 요약은 500자 이내의 짧은 문장 여러 개로 구성해.\n"
            + "8. 장소명 앞에 지명(시·군·구·동)을 쓸 때는 다음 규칙을 따라.\n"
            + "    1. 시·군·구가 바뀌면 반드시 포함한다.\n"
            + "    2. 같은 시·군·구 내에서는 동과 장소명만 쓴다.\n"
            + "영어로 변역해서 응답한다.\n";

    public static final String TRANSLATE_PLACE_INFO = """
        You are a translation assistant. Please translate the following place information from Korean to %s.
                
        Place Name: %s
        Road Address: %s
                
        Return ONLY in JSON format like:
        {
          "translatedName": "...",
          "translatedRoadAddress": "..."
        }
        """;

    public static final String TRANSLATE_PLACE_INFO_BATCH_PROMPT =
        "You are a translator. Translate this list of places from Korean to %s. " +
            "Respond ONLY as a JSON array with 'id', 'translatedName', 'translatedRoadAddress'.\n" +
            "%s";
}
