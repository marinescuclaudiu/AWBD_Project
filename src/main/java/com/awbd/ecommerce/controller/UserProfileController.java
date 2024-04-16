package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserProfileController {
    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("admin_user/user_profile")
    public ResponseEntity<UserProfileDTO> save(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok().body(userProfileService.save(userProfileDTO));
    }

    @GetMapping("admin/user_profile/{id}")
    public ResponseEntity<UserProfileDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userProfileService.findById(id));
    }

    @GetMapping("admin/user_profiles")
    public ResponseEntity<List<UserProfileDTO>> getAll() {
        List<UserProfileDTO> userProfileDTOS = userProfileService.findAll();
        return ResponseEntity.ok().body(userProfileDTOS);
    }

    @PatchMapping("admin_user/user_profile/{id}")
    public ResponseEntity<UserProfileDTO> update(@PathVariable Long id, @Valid @RequestBody UserProfileDTO userProfileDTO){
        return ResponseEntity.ok().body(userProfileService.update(id, userProfileDTO));
    }

    @DeleteMapping("admin_user/user_profile/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
