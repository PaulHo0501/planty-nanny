package com.doubletrouble.plantynanny.entity;

import com.doubletrouble.plantynanny.enums.HealthCondition;
import jakarta.persistence.*;
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
    HealthCondition healthCondition;

    @Column(columnDefinition = "TEXT")
    String description;
    @CreatedDate
    private LocalDateTime createdAt;
}
