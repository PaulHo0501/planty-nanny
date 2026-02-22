package com.doubletrouble.plantynanny.repositorty;

import com.doubletrouble.plantynanny.entity.LightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LightStatusRepository extends JpaRepository<LightStatus, UUID> {
    LightStatus findTopByOrderByCreatedAtDesc();
    List<LightStatus> findByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime start, LocalDateTime end);
}