package com.awbd.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping("public/order")
    public ResponseEntity<String> createOrder(@RequestBody String request) {
        return ResponseEntity.ok().body(request);
    }
}