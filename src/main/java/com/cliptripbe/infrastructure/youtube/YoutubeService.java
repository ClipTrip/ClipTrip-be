package com.cliptripbe.infrastructure.youtube;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeService {

    @Qualifier("youtubeWebClient")
    private final WebClient youtubeWebClient;

    @PostConstruct
    public void init() {
        if (youtubeWebClient == null) {
            throw new IllegalStateException("youtubeWebClient 주입 실패");
        }
        System.out.println("youtubeWebClient 빈 주입 완료: " + youtubeWebClient);
    }

    public Mono<String> extractPlainCaptions(String youtubeUrl) {
        String videoId = YoutubeUtils.extractVideoId(youtubeUrl);
        String listUrl = "https://video.google.com/timedtext?type=list&v=" + videoId;

        return youtubeWebClient.get()
            .uri(listUrl)
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(xml -> {
                Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
                Element firstTrack = doc.selectFirst("track");

                if (firstTrack == null) {
                    return Mono.just("");
                }

                String lang = firstTrack.attr("lang_code");
                String nameParam = firstTrack.hasAttr("name")
                    ? "&name=" + firstTrack.attr("name")
                    : "";

                String captionUrl = String.format(
                    "https://video.google.com/timedtext?lang=%s&v=%s%s",
                    lang, videoId, nameParam
                );
                log.error("진짜 이상해: {}", captionUrl);

                return youtubeWebClient.get()
                    .uri(captionUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(YoutubeUtils::parseXmlToText)
                    .map(lines -> String.join("\n", lines));
            });
    }


}
