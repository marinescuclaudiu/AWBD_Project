package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-profiles")
public class UserProfileController {
    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileDTO> save(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok().body(userProfileService.save(userProfileDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userProfileService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getAll() {
        List<UserProfileDTO> userProfileDTOS = userProfileService.findAll();
        return ResponseEntity.ok().body(userProfileDTOS);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserProfileDTO> update(@PathVariable Long id, @Valid @RequestBody UserProfileDTO userProfileDTO){
        return ResponseEntity.ok().body(userProfileService.update(id, userProfileDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
