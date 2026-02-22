package com.doubletrouble.plantynanny.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class WaterLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int percentage;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
