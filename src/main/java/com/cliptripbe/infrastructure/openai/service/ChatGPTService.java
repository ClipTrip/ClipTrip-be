package com.cliptripbe.infrastructure.openai.service;

import com.cliptripbe.infrastructure.openai.dto.ChatGPTRequest;
import com.cliptripbe.infrastructure.openai.dto.ChatGPTResponse;
import com.cliptripbe.infrastructure.openai.dto.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTService {

    @Qualifier("openAIWebClient")
    private final WebClient openAIWebClient;

    public Mono<String> ask(String userMessage) {
        Message message = Message.builder()
            .role("user")
            .content(userMessage)
            .build();

        ChatGPTRequest request = ChatGPTRequest.builder()
            .model("gpt-4.1-nano")
            .messages(List.of(message))
            .build();

        long start = System.currentTimeMillis();

        return openAIWebClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ChatGPTResponse.class)
            .map(resp -> {
                return resp.getChoices().get(0).getMessage().getContent();
            })
            .doOnSuccess(result -> {
                long elapsed = System.currentTimeMillis() - start;
                log.info("chatGPT 성공 레이턴시: {} ms", elapsed);
            });
    }

}
