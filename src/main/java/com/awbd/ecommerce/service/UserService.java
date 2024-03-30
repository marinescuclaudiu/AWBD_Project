package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO save(UserDTO user);
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    void deleteById(Long id);
    UserDTO update(Long id, UserDTO user);
}
