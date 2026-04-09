package com.example.collections_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.collections_api.entity.CollectionItem;
import com.example.collections_api.repository.CollectionRepository;
import com.example.collections_api.repository.ItemRepository;

@RestController
public class ItemController {

    @Autowired ItemRepository itemRepo;
    @Autowired CollectionRepository collectionRepo;

    private Long userId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean ownsCollection(Long collectionId) {
        return collectionRepo.findByIdAndUserId(collectionId, userId()).isPresent();
    }

    @GetMapping("/items")
    public ResponseEntity<?> getByCollection(@RequestParam Long collectionId) {
        if (!ownsCollection(collectionId))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(itemRepo.findByCollectionId(collectionId));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        CollectionItem item = itemRepo.findById(id).orElse(null);
        if (item == null || !ownsCollection(item.collectionId))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @PostMapping("/items")
    public ResponseEntity<?> create(@RequestBody CollectionItem body) {
        if (!ownsCollection(body.collectionId))
            return ResponseEntity.notFound().build();
        itemRepo.save(body);
        return ResponseEntity.ok(Map.of("id", body.id));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> replace(@PathVariable Long id, @RequestBody CollectionItem body) {
        CollectionItem item = itemRepo.findById(id).orElse(null);
        if (item == null || !ownsCollection(item.collectionId))
            return ResponseEntity.notFound().build();
        body.id = id;
        body.collectionId = item.collectionId;
        itemRepo.save(body);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        CollectionItem item = itemRepo.findById(id).orElse(null);
        if (item == null || !ownsCollection(item.collectionId))
            return ResponseEntity.notFound().build();
        if (body.containsKey("name"))        item.name        = (String) body.get("name");
        if (body.containsKey("description")) item.description = (String) body.get("description");
        if (body.containsKey("image"))       item.image       = (String) body.get("image");
        if (body.containsKey("rarity"))      item.rarity      = (String) body.get("rarity");
        if (body.containsKey("price"))       item.price       = ((Number) body.get("price")).doubleValue();
        itemRepo.save(item);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        CollectionItem item = itemRepo.findById(id).orElse(null);
        if (item == null || !ownsCollection(item.collectionId))
            return ResponseEntity.notFound().build();
        itemRepo.delete(item);
        return ResponseEntity.noContent().build();
    }
}