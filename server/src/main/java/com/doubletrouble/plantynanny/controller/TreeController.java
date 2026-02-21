package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.dto.TreeDto;
import com.doubletrouble.plantynanny.entity.Tree;
import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import com.doubletrouble.plantynanny.service.GeminiService;
import com.doubletrouble.plantynanny.service.ImageBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/tree")
public class TreeController {
    @Autowired
    private TreeRepository treeRepository;

    private final GeminiService geminiService;

    private final ImageBridgeService imageBridgeService;

    public TreeController(GeminiService geminiService, ImageBridgeService imageBridgeService) {
        this.geminiService = geminiService;
        this.imageBridgeService = imageBridgeService;
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

}