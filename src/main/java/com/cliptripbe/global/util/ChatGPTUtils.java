package com.cliptripbe.global.util;

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

    public static String removeLiteralNewlines(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\n", "");
    }
}
