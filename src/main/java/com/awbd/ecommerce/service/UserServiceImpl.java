package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.model.User;
import com.awbd.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO save(UserDTO user) {
        User savedUser = userRepository.save(modelMapper.map(user, User.class));
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User with id " + id + " doesn't exist"));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with id " + id + " doesn't exist");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO update(Long id, UserDTO user) {
        return null;
    }
}
