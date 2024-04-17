package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("admin/user/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping("admin/users")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> userDTOS = userService.findAll();
        return ResponseEntity.ok().body(userDTOS);
    }

    @PatchMapping("admin_user/user/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok().body(userService.update(id, userDTO));
    }

    @DeleteMapping("admin_user/user/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
