package com.doubletrouble.plantynanny.dto;

public record TreeDto(String name,
                      String description,
                      Integer humidityLevel,
                      Integer lightHours) {
}