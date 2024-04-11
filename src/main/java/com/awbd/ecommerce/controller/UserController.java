package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.save(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> userDTOS = userService.findAll();
        return ResponseEntity.ok().body(userDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        return ResponseEntity.ok().body(userService.update(id, userDTO));
    }
}
