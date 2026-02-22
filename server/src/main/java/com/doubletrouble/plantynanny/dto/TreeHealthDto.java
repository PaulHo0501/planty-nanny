package com.doubletrouble.plantynanny.dto;

import com.doubletrouble.plantynanny.enums.HealthCondition;

public record TreeHealthDto(String imageUrl,
                            HealthCondition healthCondition,
                            String description) {
}
