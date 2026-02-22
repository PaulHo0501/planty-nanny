package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.dto.TreeDto;
import com.doubletrouble.plantynanny.dto.TreeHealthDto;
import com.doubletrouble.plantynanny.entity.LightStatus;
import com.doubletrouble.plantynanny.entity.Tree;
import com.doubletrouble.plantynanny.entity.TreeHealth;
import com.doubletrouble.plantynanny.enums.LightState;
import com.doubletrouble.plantynanny.repositorty.LightStatusRepository;
import com.doubletrouble.plantynanny.repositorty.TreeHealthRepository;
import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import com.doubletrouble.plantynanny.service.GeminiService;
import com.doubletrouble.plantynanny.service.ImageBridgeService;
import com.doubletrouble.plantynanny.service.LightStatusService;
import com.doubletrouble.plantynanny.service.TreeHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/tree")
public class MainController {
    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private TreeHealthRepository treeHealthRepository;

    @Autowired
    private LightStatusRepository lightStatusRepository;

    private final GeminiService geminiService;

    private final ImageBridgeService imageBridgeService;

    private final TreeHealthService treeHealthService;

    private final LightStatusService lightStatusService;

    public MainController(GeminiService geminiService,
                          ImageBridgeService imageBridgeService,
                          TreeHealthService treeHealthService,
                          LightStatusService lightStatusService) {
        this.geminiService = geminiService;
        this.imageBridgeService = imageBridgeService;
        this.treeHealthService = treeHealthService;
        this.lightStatusService = lightStatusService;

    }


    @GetMapping("/get-tree")
    public ResponseEntity<Tree> getTree(@RequestParam("id") String id) {
        try {
            CompletableFuture<String> futureS3Url = imageBridgeService.triggerCaptureAndWait(id);

            String s3Url = futureS3Url.get(15, TimeUnit.SECONDS);

            TreeDto analysisData = geminiService.analyzePlantImage(s3Url);

            Tree newTree = new Tree();
            newTree.setName(analysisData.name());
            newTree.setDescription(analysisData.description());
            newTree.setHumidity_level(analysisData.humidityLevel());
            newTree.setLight_hours(analysisData.lightHours());
            newTree.setImageURL(s3Url);
            Tree savedTree = treeRepository.save(newTree);

            TreeHealthDto analysisHealthData = geminiService.analyzePlantHealth(s3Url);
            TreeHealth health = new TreeHealth();
            health.setImageUrl(s3Url);
            health.setHealthCondition(analysisHealthData.healthCondition());
            health.setDescription(analysisHealthData.description());
            treeHealthRepository.save(health);

            return ResponseEntity.ok(savedTree);

        } catch (TimeoutException e) {
            System.err.println("ESP32 took too long to capture and upload!");
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            System.err.println("Error processing tree request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/generate-fact")
    public ResponseEntity<String> getFact() {
        String name = treeRepository.findFirstBy().map(Tree::getName).orElse(null);
        if (!Objects.isNull(name)) {
            String generatedFact  = geminiService.askGemini("Give me a random, short and cool fact about %s. Your response should be a single sentence only, have no markdown marker, just pure text.".formatted(name));
            System.out.println("Fact generated: " + generatedFact);
            return ResponseEntity.ok(generatedFact);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/analyze-health")
    public ResponseEntity<TreeHealth> analyzeHealth(@RequestParam("id") String id, @RequestParam(value = "manual", defaultValue = "false") boolean manual) {
        try {
            if (manual) {
                TreeHealth savedHealthCondition = treeHealthService.executeHealthAnalysis(id);
                return ResponseEntity.ok(savedHealthCondition);
            } else {
                TreeHealth latestHealth = treeHealthService.getLatestHealthRecord();
                return ResponseEntity.ok(latestHealth);
            }

        } catch (TimeoutException e) {
            System.err.println("ESP32 took too long to capture and upload!");
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            System.err.println("Error processing tree request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/light-status")
    public ResponseEntity<String> updateLightStatus(@RequestParam("id") String id, @RequestParam("light-status") LightState lightStatus) {
        String response = lightStatusService.turnLightOnOff(id, lightStatus);

        LightStatus savedLightStatus = new LightStatus();
        savedLightStatus.setLightStatus(lightStatus);

        lightStatusRepository.save(savedLightStatus);
        return ResponseEntity.ok(response);
    }

}