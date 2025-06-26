package com.cliptripbe.infrastructure.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatGPTRequest {

    private String model;

    private List<Message> messages;
}
