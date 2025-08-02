package com.cliptripbe.infrastructure.adapter.out.openai;

import static com.cliptripbe.global.response.type.ErrorType.CHATGPT_NO_RESPONSE;

import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.adapter.out.openai.dto.ChatGptRequest;
import com.cliptripbe.infrastructure.adapter.out.openai.dto.ChatGptResponse;
import com.cliptripbe.infrastructure.adapter.out.openai.dto.ChatGptResponse.Choice;
import com.cliptripbe.infrastructure.adapter.out.openai.dto.Message;
import com.cliptripbe.infrastructure.port.openai.ChatGptPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGptAdapter implements ChatGptPort {

    @Qualifier("openAIRestClient")
    private final RestClient openAIRestClient;

    @Override
    public String askPlaceExtraction(String userMessage) {
        Message message = Message.builder()
            .role("user")
            .content(userMessage)
            .build();

        ChatGptRequest request = ChatGptRequest.builder()
            .model("gpt-4.1-mini")
            .messages(List.of(message))
            .temperature(0.0)
            .max_tokens(350)
            .frequency_penalty(0.0)
            .build();

        long start = System.currentTimeMillis();

        ChatGptResponse response = openAIRestClient.post()
            .uri("/chat/completions")
            .body(request)
            .retrieve()
            .body(ChatGptResponse.class);

        String result = Optional.ofNullable(response)
            .map(ChatGptResponse::getChoices)
            .filter(choices -> !choices.isEmpty())
            .map(List::getFirst)
            .map(Choice::getMessage)
            .map(Message::getContent)
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        long elapsed = System.currentTimeMillis() - start;
        log.info("chatGPT 성공 레이턴시: {} ms", elapsed);

        return result;
    }

    @Override
    public String ask(String userMessage) {
        Message message = Message.builder()
            .role("user")
            .content(userMessage)
            .build();

        ChatGptRequest request = ChatGptRequest.builder()
            .model("gpt-4.1-nano")
            .messages(List.of(message))
            .temperature(0.0)
            .max_tokens(350)
            .frequency_penalty(0.0)
            .build();

        long start = System.currentTimeMillis();

        ChatGptResponse response = openAIRestClient.post()
            .uri("/chat/completions")
            .body(request)
            .retrieve()
            .body(ChatGptResponse.class);

        String result = Optional.ofNullable(response)
            .map(ChatGptResponse::getChoices)
            .filter(choices -> !choices.isEmpty())
            .map(List::getFirst)
            .map(Choice::getMessage)
            .map(Message::getContent)
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        long elapsed = System.currentTimeMillis() - start;
        log.info("chatGPT 성공 레이턴시: {} ms", elapsed);

        return result;
    }

}
