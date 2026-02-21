package com.doubletrouble.plantynanny.controller;

import com.doubletrouble.plantynanny.entity.Tree;
import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import com.doubletrouble.plantynanny.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/tree")
public class TreeController {
    @Autowired
    private TreeRepository treeRepository;

    private final GeminiService geminiService;

    public TreeController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/generate-fact")
    public ResponseEntity<String> getFact() {
        String name = treeRepository.findFirstBy().map(Tree::getName).orElse(null);
        if (!Objects.isNull(name)) {
            String generatedFact  = geminiService.askGemini("Give me a random, short and cool fact about %s. Your response should be a single sentence only, have no markdown marker, just pure text.".formatted(name));
            return ResponseEntity.ok(generatedFact);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/create-tree")
    public ResponseEntity<String> createTree() {
        Tree tree = new Tree();
        tree.setName("Fiddle Leaf Fig");
        tree.setDescription("A popular indoor tree with large, heavily veined, violin-shaped leaves.");
        tree.setImageURL("https://example.com/fiddle.jpg");
        tree.setHumidity_level(60);
        tree.setLight_hours(6);
        treeRepository.save(tree);

        return ResponseEntity.ok("Tree created successfully");
    }
}