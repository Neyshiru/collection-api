package com.example.collections_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collections_api.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
    Optional<Collection> findByIdAndUserId(Long id, Long userId);
}