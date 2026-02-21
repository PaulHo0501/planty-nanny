package com.doubletrouble.plantynanny.service;

import com.doubletrouble.plantynanny.dto.TreeDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.net.MalformedURLException;

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

    public TreeDto analyzePlantImage(String s3ImageUrl) {
        String promptText = "Analyze this image and identify the plant. " +
                "Provide the common name of the tree/plant, a short description, " +
                "the suggested humidity level (as an integer percentage), " +
                "and the suggested light hours per day (as an integer).";

        return chatClient.prompt()
                .user(userSpec -> {
                            try {
                                userSpec
                                        .text(promptText)
                                        .media(MimeTypeUtils.IMAGE_JPEG, new UrlResource(s3ImageUrl));
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .call()
                .entity(TreeDto.class);

    }
}