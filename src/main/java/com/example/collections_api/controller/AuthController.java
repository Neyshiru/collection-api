package com.example.collections_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.collections_api.entity.User;
import com.example.collections_api.repository.UserRepository;
import com.example.collections_api.security.JwtService;

@RestController
public class AuthController {

    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtService jwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        User user = userRepo.findByUsername(body.get("username")).orElse(null);

        if (user == null || !encoder.matches(body.get("password"), user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid credentials"));
        }

        return ResponseEntity.ok(Map.of(
                "token", jwt.generate(user.id)
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of(
            "id", user.id,
            "username", user.username,
            "firstname", user.firstname,
            "lastname", user.lastname
        ));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("success", true));
    }
}