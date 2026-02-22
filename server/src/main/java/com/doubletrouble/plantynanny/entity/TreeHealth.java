package com.doubletrouble.plantynanny.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class TreeHealth {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String imageUrl;
    Boolean condition;
    String description;
    @CreatedDate
    private LocalDateTime createdAt;
}
