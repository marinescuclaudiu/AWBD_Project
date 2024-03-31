package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserProfileDTO;

import java.util.List;

public interface UserProfileService {
    UserProfileDTO save(UserProfileDTO userProfileDTO);
    List<UserProfileDTO> findAll();
    UserProfileDTO findById(Long id);
    void deleteById(Long id);
    UserProfileDTO update(Long id, UserProfileDTO userProfileDTO);
}
