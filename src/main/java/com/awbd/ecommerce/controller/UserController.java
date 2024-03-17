package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.model.User;
import com.awbd.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("public/order")
    public ResponseEntity<String> createOrder(@RequestBody String request) {
        return ResponseEntity.ok().body(request);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok().body(userService.findAll());
    }
}