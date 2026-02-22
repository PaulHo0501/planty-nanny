package com.doubletrouble.plantynanny.service;

import com.doubletrouble.plantynanny.entity.LightStatus;
import com.doubletrouble.plantynanny.entity.Tree;
import com.doubletrouble.plantynanny.enums.LightState;
import com.doubletrouble.plantynanny.repositorty.LightStatusRepository;
import com.doubletrouble.plantynanny.repositorty.TreeRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class LightStatusService {

    private final LightStatusRepository lightStatusRepository;
    private final TreeRepository treeRepository;
    private final ImageBridgeService imageBridgeService;
    private final TaskScheduler taskScheduler;

    public LightStatusService(ImageBridgeService imageBridgeService,
                              LightStatusRepository lightStatusRepository,
                              TreeRepository treeRepository,
                              TaskScheduler taskScheduler) {
        this.imageBridgeService = imageBridgeService;
        this.lightStatusRepository = lightStatusRepository;
        this.treeRepository = treeRepository;
        this.taskScheduler = taskScheduler;
    }

    public String turnLightOnOff(String cameraId, LightState lightStatus) {
        imageBridgeService.toggleLight(cameraId, lightStatus);
        LightStatus newLightStatus = new LightStatus();
        newLightStatus.setLightStatus(lightStatus);
        lightStatusRepository.save(newLightStatus);
        return newLightStatus.getLightStatus().name();
    }

    // @Scheduled(cron = "0 0 7 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void scheduleLightOn() {
        if (treeRepository.count() == 0) {
            System.out.println("No plants registered. Skipping 7 AM Light ON.");
            return;
        }

        Optional<Tree> optionalTree = treeRepository.findFirstBy();
        if (optionalTree.isPresent()) {
            Tree myTree = optionalTree.get();
            Integer idealLightHours = myTree.getLight_hours();

            System.out.println("Turning ESP32 light ON.");
            try {
                turnLightOnOff("esp32_cam_1", LightState.ON);
            } catch (Exception e) {
                System.err.println("Failed to turn on morning light: " + e.getMessage());
            }

//            Instant turnOffTime = Instant.now().plus(idealLightHours, ChronoUnit.HOURS);
            Instant turnOffTime = Instant.now().plus(idealLightHours, ChronoUnit.SECONDS);
            System.out.println("Light scheduled to turn OFF dynamically at: " + turnOffTime);

            taskScheduler.schedule(() -> {
                System.out.println("Turning ESP32 light OFF.");
                try {
                    turnLightOnOff("esp32_cam_1", LightState.OFF);
                } catch (Exception e) {
                    System.err.println("Failed to turn off light: " + e.getMessage());
                }
            }, turnOffTime);
        }
    }
}