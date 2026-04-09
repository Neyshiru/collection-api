package com.example.collections_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collections_api.entity.CollectionItem;

public interface ItemRepository extends JpaRepository<CollectionItem, Long> {
    List<CollectionItem> findByCollectionId(Long collectionId);
}