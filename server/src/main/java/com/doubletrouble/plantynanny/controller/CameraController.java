package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.dto.CameraPayload;
import com.doubletrouble.plantynanny.service.ImageBridgeService;
import com.doubletrouble.plantynanny.service.S3Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private final SimpMessagingTemplate messagingTemplate;
    private final S3Service s3Service;
    private final ImageBridgeService imageBridgeService;

    public CameraController(SimpMessagingTemplate messagingTemplate, S3Service s3Service,  ImageBridgeService imageBridgeService) {
        this.messagingTemplate = messagingTemplate;
        this.s3Service = s3Service;
        this.imageBridgeService = imageBridgeService;
    }

    @PostMapping("/upload")
    public String handleRawImageUpload(@RequestParam(value = "id", defaultValue = "esp32_cam_1") String id,
                                       @RequestBody byte[] imageBytes) {
        System.out.println("Received raw image bytes via HTTP! Size: " + imageBytes.length);

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "plant_cam_" + timestamp + ".jpg";

            String s3Url = s3Service.uploadImage(imageBytes, fileName);
            System.out.println("Image successfully saved to S3: " + s3Url);

            imageBridgeService.completeCapture(id, s3Url);

            CameraPayload payload = new CameraPayload();
            payload.setType("NEW_IMAGE");
            payload.setData(s3Url);

            messagingTemplate.convertAndSend("/topic/images", payload);

            return "Upload successful!";

        } catch (Exception e) {
            System.err.println("Failed to upload to S3: " + e.getMessage());

            return "Upload failed!";
        }
    }
}