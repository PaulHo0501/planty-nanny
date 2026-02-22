package com.doubletrouble.plantynanny.repositorty;

import com.doubletrouble.plantynanny.entity.LightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LightStatusRepository extends JpaRepository<LightStatus, UUID> {
    LightStatus findTopByOrderByCreatedAtDesc();
}