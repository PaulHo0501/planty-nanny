package com.doubletrouble.plantynanny.dto;

public record TreeHealthDto(String imageUrl,
                            Boolean condition,
                            String description) {
}
