package com.doubletrouble.plantynanny.repositorty;

import com.doubletrouble.plantynanny.entity.TreeHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreeHealthRepository extends JpaRepository<TreeHealth, UUID> {
    Optional<TreeHealth> findFirstByOrderByCreatedAtDesc();
}
