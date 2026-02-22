package com.doubletrouble.plantynanny.service;

import com.doubletrouble.plantynanny.entity.LightStatus;
import com.doubletrouble.plantynanny.enums.LightState;
import com.doubletrouble.plantynanny.repositorty.LightStatusRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class LightStatusAnalyticsService {
    private final LightStatusRepository repository;

    public LightStatusAnalyticsService(LightStatusRepository repository) {
        this.repository = repository;
    }

    public int getTodayTotalOnHours() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<LightStatus> todayEvents = repository.findByCreatedAtBetweenOrderByCreatedAtAsc(startOfDay, now);

        long totalSecondsOn = 0;
        LocalDateTime currentOnTime = null;

        for (LightStatus event : todayEvents) {
            if (event.getLightStatus() == LightState.ON) {
                currentOnTime = event.getCreatedAt();
            } else if (event.getLightStatus() == LightState.OFF) {
                if (currentOnTime != null) {
                    totalSecondsOn += Duration.between(currentOnTime, event.getCreatedAt()).getSeconds();
                    currentOnTime = null;
                } else {
                    totalSecondsOn += Duration.between(startOfDay, event.getCreatedAt()).getSeconds();
                }
            }
        }

        if (currentOnTime != null) {
            totalSecondsOn += Duration.between(currentOnTime, now).getSeconds();
        }
        else if (todayEvents.isEmpty()) {
            LightStatus lastKnownState = repository.findTopByOrderByCreatedAtDesc();
            if (lastKnownState != null && lastKnownState.getLightStatus() == LightState.ON) {
                totalSecondsOn += Duration.between(startOfDay, now).getSeconds();
            }
        }
        return Math.round(totalSecondsOn);
    }
}