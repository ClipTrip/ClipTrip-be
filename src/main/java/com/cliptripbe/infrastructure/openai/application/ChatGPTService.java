package com.cliptripbe.infrastructure.openai.application;

import com.cliptripbe.infrastructure.openai.dto.ChatGPTRequest;
import com.cliptripbe.infrastructure.openai.dto.ChatGPTResponse;
import com.cliptripbe.infrastructure.openai.dto.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
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
            .model("gpt-4o-mini")
            .messages(List.of(message))
            .build();

        return openAIWebClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ChatGPTResponse.class)
            .map(resp -> {
                return resp.getChoices().get(0).getMessage().getContent();
            });
    }

}
