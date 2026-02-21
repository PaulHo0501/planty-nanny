package com.doubletrouble.plantynanny.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImageBridgeService {
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    public ImageBridgeService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public CompletableFuture<String> triggerCaptureAndWait(String esp32Id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        pendingRequests.put(esp32Id, future);

        String commandJson = String.format("{\"command\":\"PICTURE\", \"id\":\"%s\"}", esp32Id);
        messagingTemplate.convertAndSend("/topic/commands", commandJson);

        return future;
    }

    public void completeCapture(String esp32Id, String s3Url) {
        CompletableFuture<String> future = pendingRequests.remove(esp32Id);
        if (future != null) {
            future.complete(s3Url);
        }
    }
}