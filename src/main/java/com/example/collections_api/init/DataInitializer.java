package com.example.collections_api.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.collections_api.entity.*;
import com.example.collections_api.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired UserRepository userRepo;
    @Autowired CollectionRepository collectionRepo;
    @Autowired PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        User admin = userRepo.findByUsername("admin").orElse(null);

        if (admin == null) {
            admin = new User();
            admin.username = "admin";
            admin.password = encoder.encode("admin1234");
            admin.firstname = "Super";
            admin.lastname = "Admin";
            userRepo.save(admin);

            System.out.println("✅ Admin created");
        }

        List<Collection> collections = collectionRepo.findByUserId(admin.id);

        if (collections.isEmpty()) {
            Collection c = new Collection();
            c.title = "Default collection";
            c.userId = admin.id;
            collectionRepo.save(c);

            System.out.println("✅ Default collection created");
        }
    }
}