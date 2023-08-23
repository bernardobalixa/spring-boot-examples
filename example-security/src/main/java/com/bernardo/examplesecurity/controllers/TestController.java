package com.bernardo.examplesecurity.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/free")
    public ResponseEntity<String> freeRoute() {
        return ResponseEntity.ok().body("This is a free route.");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userRoute() {
        return ResponseEntity.ok().body("This is a route that needs authentication from user or admin role.");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminRoute() {
        return ResponseEntity.ok().body("This is a route that needs authentication from admin role.");
    }

}
