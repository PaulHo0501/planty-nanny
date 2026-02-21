package com.doubletrouble.plantynanny.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final ChatClient chatClient;

    public GeminiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String askGemini(String userMessage) {
        return chatClient.prompt(userMessage)
                .call()
                .content();
    }
}