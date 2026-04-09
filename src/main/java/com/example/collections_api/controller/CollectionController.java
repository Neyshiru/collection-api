package com.example.collections_api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.collections_api.entity.Collection;
import com.example.collections_api.repository.CollectionRepository;
import com.example.collections_api.repository.ItemRepository;

@RestController
public class CollectionController {

    @Autowired CollectionRepository collectionRepo;
    @Autowired ItemRepository itemRepo;

    private Long userId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/collections")
    public List<Map<String, Object>> getAll() {
        return collectionRepo.findByUserId(userId()).stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.id);
            map.put("title", c.title);
            map.put("itemsCount", itemRepo.findByCollectionId(c.id).size());
            return map;
        }).toList();
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Collection c = collectionRepo.findByIdAndUserId(id, userId()).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of(
            "id", c.id,
            "title", c.title,
            "itemsCount", itemRepo.findByCollectionId(c.id).size(),
            "items", itemRepo.findByCollectionId(c.id)
        ));
    }

    @PostMapping("/collections")
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        Collection c = new Collection();
        c.title = body.get("title");
        c.userId = userId();
        collectionRepo.save(c);
        return ResponseEntity.ok(Map.of("id", c.id));
    }

    @PutMapping("/collections/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Collection c = collectionRepo.findByIdAndUserId(id, userId()).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();
        c.title = body.get("title");
        collectionRepo.save(c);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/collections/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Collection c = collectionRepo.findByIdAndUserId(id, userId()).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();
        collectionRepo.delete(c);
        return ResponseEntity.noContent().build();
    }
}