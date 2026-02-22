package com.doubletrouble.plantynanny.service;

import com.doubletrouble.plantynanny.entity.TreeHealth;
import com.doubletrouble.plantynanny.repositorty.TreeHealthRepository;
import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class TreeHealthService {
    private final ImageBridgeService imageBridgeService;
    private final GeminiService geminiService;
    private final TreeHealthRepository treeHealthRepository;
    private final TreeRepository treeRepository;

    public TreeHealthService(ImageBridgeService imageBridgeService,
                             GeminiService geminiService,
                             TreeHealthRepository treeHealthRepository,
                             TreeRepository treeRepository) {
        this.imageBridgeService = imageBridgeService;
        this.geminiService = geminiService;
        this.treeHealthRepository = treeHealthRepository;
        this.treeRepository = treeRepository;
    }

    public TreeHealth executeHealthAnalysis(String cameraId) throws Exception {
        CompletableFuture<String> futureS3Url = imageBridgeService.triggerCaptureAndWait(cameraId);
        String s3Url = futureS3Url.get(15, TimeUnit.SECONDS);

        com.doubletrouble.plantynanny.dto.TreeHealthDto analysisData = geminiService.analyzePlantHealth(s3Url);

        TreeHealth health = new TreeHealth();
        health.setImageUrl(s3Url);
        health.setHealthCondition(analysisData.healthCondition());
        health.setDescription(analysisData.description());

        return treeHealthRepository.save(health);
    }


    // @Scheduled(cron = "0 0 10 * * ?")
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void scheduledDailyHealthCheck() {
        if (treeRepository.count() == 0) {
            System.out.println("No plants registered yet. Skipping scheduled ESP32 health check.");
            return; // This instantly kills the method so nothing else runs!
        }
        System.out.println("Starting automatic daily plant health check...");
        try {
            TreeHealth result = executeHealthAnalysis("esp32_cam_1");
            System.out.println("Daily check complete! Plant is healthy: " + result.getHealthCondition());

        } catch (TimeoutException e) {
            System.err.println("Scheduled Check Failed: ESP32 took too long or is offline!");
        } catch (Exception e) {
            System.err.println("Scheduled Check Failed: " + e.getMessage());
        }
    }

    public TreeHealth getLatestHealthRecord() {
        return treeHealthRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No health records exist in the database yet!"));
    }
}
