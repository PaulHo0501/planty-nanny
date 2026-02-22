package com.doubletrouble.plantynanny.service;


import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HumidityService {
    private final SimpMessagingTemplate messagingTemplate;
    private final TreeRepository treeRepository;

    public HumidityService(SimpMessagingTemplate messagingTemplate, TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 300000)
    public void triggerEsp32Measurement() {
        if (treeRepository.count() == 0) {
            System.out.println("No plants registered. Skipping MEASUREMENT.");
            return;
        }
        String commandJson = "{\"command\":\"MEASURE\", \"id\":\"esp32_cam_1\", \"type\":\"humidity\"}";
        messagingTemplate.convertAndSend("/topic/commands", commandJson);
        System.out.println("Sent MEASURE command to ESP32.");
    }

    @Scheduled(fixedRate = 300000)
    public void triggerEsp32MeasurementWaterLevel() {
        String waterCommand = "{\"command\":\"MEASURE\", \"id\":\"esp32_cam_1\", \"type\":\"water\"}";
        messagingTemplate.convertAndSend("/topic/commands", waterCommand);
        System.out.println("Sent MEASURE Water Level command to ESP32.");
    }
}