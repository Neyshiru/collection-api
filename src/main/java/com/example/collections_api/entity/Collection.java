package com.example.collections_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    @Column(name = "user_id")
    public Long userId;
}