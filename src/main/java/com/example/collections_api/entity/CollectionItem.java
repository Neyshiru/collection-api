package com.example.collections_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "collection_items")
public class CollectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "collection_id")
    public Long collectionId;

    public String name;
    public String description;

    @Column(columnDefinition = "TEXT")
    public String image;
    public String rarity;
    public Double price;
}