package com.elearning.haui.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.ImageStorage;
@Repository
public interface ImageStorageRepository extends JpaRepository<ImageStorage,Long> {
     Optional<ImageStorage> findByHash(String hash);
    
}
