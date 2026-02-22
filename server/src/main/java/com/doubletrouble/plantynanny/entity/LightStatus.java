package com.doubletrouble.plantynanny.entity;

import com.doubletrouble.plantynanny.enums.LightState;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class LightStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LightState lightStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

}