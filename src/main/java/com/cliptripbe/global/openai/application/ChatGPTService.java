package com.cliptripbe.global.openai.application;

import com.cliptripbe.global.openai.dto.ChatGPTRequest;
import com.cliptripbe.global.openai.dto.ChatGPTResponse;
import com.cliptripbe.global.openai.dto.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final WebClient webClient;

    public Mono<String> ask(String userMessage) {
        ChatGPTRequest request = new ChatGPTRequest(
            "gpt-4o-mini",
            List.of(new Message("user", userMessage))
        );

        return webClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ChatGPTResponse.class)
            .map(resp -> {
                // 첫 번째 choice 의 메시지 내용만 꺼내서 리턴
                return resp.getChoices().get(0).getMessage().getContent();
            });
    }

}
