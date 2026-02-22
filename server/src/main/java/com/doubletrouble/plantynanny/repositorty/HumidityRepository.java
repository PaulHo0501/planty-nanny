package com.doubletrouble.plantynanny.repositorty;

import com.doubletrouble.plantynanny.entity.Humidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HumidityRepository extends JpaRepository<Humidity, UUID> {
    List<Humidity> findTop8ByOrderByCreatedAtDesc();
    Humidity findTopByOrderByCreatedAtDesc();
}