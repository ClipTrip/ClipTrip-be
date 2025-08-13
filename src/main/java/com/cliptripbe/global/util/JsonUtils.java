package com.cliptripbe.global.util;

import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtils {

    private final ObjectMapper objectMapper;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }

    public TranslationInfo readValue(String response, Class<TranslationInfo> translationInfoClass) {
        try {
            return objectMapper.readValue(response, translationInfoClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("번역 결과 파싱 실패", e);
        }
    }

    public <T> List<T> parseToList(String responseJson, Class<T> clazz) {
        try {
            CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz);
            return objectMapper.readValue(responseJson, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 역직렬화 실패", e);
        }
    }

}