package com.cliptripbe.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {

    private static final Pattern YT_PATTERN = Pattern.compile(
        "(?:https?://)?(?:www\\.)?" +
            "(?:youtube\\.com/(?:(?:watch\\?v=)|(?:embed/)|(?:shorts/))|youtu\\.be/)" +
            "([A-Za-z0-9_-]{11})"
    );

    public static String extractVideoId(String url) {
        Matcher matcher = YT_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("유효한 YouTube URL이 아닙니다: " + url);
    }

    public static String getThumbnailUrl(String videoId) {
        return String.format("https://img.youtube.com/vi/%s/hqdefault.jpg", videoId);
    }
}
