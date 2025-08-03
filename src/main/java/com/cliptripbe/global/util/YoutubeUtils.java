package com.cliptripbe.global.util;

import static com.cliptripbe.global.response.type.ErrorType.INVALID_YOUTUBE_URL;

import com.cliptripbe.global.response.exception.CustomException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {

    private static final Pattern YT_PATTERN = Pattern.compile(
        "(?:https?://)?(?:www\\.)?" +
            "(?:youtube\\.com/(?:(?:watch\\?v=)|(?:embed/)|(?:shorts/))|youtu\\.be/)" +
            "([A-Za-z0-9_-]{11})" +
            "(?:\\?.*)?"
    );

    public static String extractVideoId(String url) {
        Matcher matcher = YT_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new CustomException(INVALID_YOUTUBE_URL);
    }

    public static String getThumbnailUrl(String videoId) {
        return String.format("https://img.youtube.com/vi/%s/hqdefault.jpg", videoId);
    }
}
