package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.dto.CameraPayload;
import com.doubletrouble.plantynanny.service.S3Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/camera")
public class CameraController {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String UPLOAD_DIR = "esp32_images/";
    private final S3Service s3Service;

    public CameraController(SimpMessagingTemplate messagingTemplate, S3Service s3Service) {
        this.messagingTemplate = messagingTemplate;
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public String handleRawImageUpload(@RequestBody byte[] imageBytes) {
        System.out.println("Received raw image bytes via HTTP! Size: " + imageBytes.length);

        try {
            // 1. Generate the filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "plant_cam_" + timestamp + ".jpg";

            String s3Url = s3Service.uploadImage(imageBytes, fileName);
            System.out.println("Image successfully saved to S3: " + s3Url);

            CameraPayload payload = new CameraPayload();
            payload.setType("NEW_IMAGE");
            payload.setData(s3Url);

            messagingTemplate.convertAndSend("/topic/images", payload);

            return "Upload successful";

        } catch (Exception e) {
            System.err.println("Failed to upload to S3: " + e.getMessage());
            return "Upload failed";
        }
    }
}