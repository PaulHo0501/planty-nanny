package com.doubletrouble.plantynanny.repositorty;

import com.doubletrouble.plantynanny.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
    Optional<Tree> findFirstBy();
}