package com.cliptripbe.infrastructure.port.openai;

public interface AiTextProcessorPort {

    String askPlaceExtraction(String userMessage);

    String ask(String userMessage);
}
