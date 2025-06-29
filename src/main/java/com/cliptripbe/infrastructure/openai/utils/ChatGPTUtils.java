package com.cliptripbe.infrastructure.openai.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatGPTUtils {

    public static List<String> extractPlaces(String raw) {
        return Arrays.stream(raw.split("\\r?\\n"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(s -> s.replaceAll("^\\d+\\.\\s*", ""))
            .collect(Collectors.toList());
    }
}
