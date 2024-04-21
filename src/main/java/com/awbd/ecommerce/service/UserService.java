package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO save(UserDTO userDTO);
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    void deleteById(Long id);
    UserDTO update(Long id, UserDTO userDTO);

    UserDTO findByUsername(String username);

    void registerNewUser(String username, String password);

    String findRoleOfUserByUserId(Long userId);
}
