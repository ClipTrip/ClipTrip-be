package com.cliptripbe.infrastructure.port.openai;

public interface ChatGptPort {

    String askPlaceExtraction(String userMessage);

    String ask(String userMessage);
}
