package com.cliptripbe.global.util;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.PlacePromptInput;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static List<PlacePromptInput> buildPromptInputs(
        List<PlaceDto> places,
        int start,
        int end
    ) {
        //**TODO 범용성 좋게 바꿀 예정
        return IntStream.range(start, end)
            .mapToObj(i -> {
                PlaceDto p = places.get(i);
                return new PlacePromptInput(i, p.placeName(), p.roadAddress());
            })
            .collect(Collectors.toList());
    }
}
