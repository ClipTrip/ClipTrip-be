package com.cliptripbe.infrastructure.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatGPTRequest {

    private String model;

    private List<Message> messages;
}
