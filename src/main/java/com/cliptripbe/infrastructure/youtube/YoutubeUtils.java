package com.cliptripbe.infrastructure.youtube;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YoutubeUtils {

    private static final Pattern YT_PATTERN =
        Pattern.compile("(?:v=|youtu\\.be/)([A-Za-z0-9_-]{11})");

    public static String extractVideoId(String url) {
        Matcher matcher = YT_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("유효한 YouTube URL이 아닙니다.");
    }

    public static List<String> parseXmlToText(String xml) {
        Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
        return doc.select("text").stream()
            .map(Element::text)
            .collect(Collectors.toList());
    }
}
