package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.User;
import com.awbd.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO.getEmail());
        User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));

        log.info("User saved: {}", userDTO.getEmail());
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();

        log.info("Found {} users", users.size());
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        log.info("Fetching user by ID: {}", id);
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            log.error("User with id {} not found!", id);
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }

        log.info("User with id {} found", user.get().getId());
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting user by ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("User with id {} not found", id);
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully");
    }

    @Transactional
    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            log.error("User with id {} not found", id);
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }

        BeanUtils.copyProperties(userDTO, user.get(), BeanHelper.getNullPropertyNames(userDTO));

        userRepository.save(user.get());

        log.info("User with id {} updated", user.get().getId());
        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findUserByEmail(username);

       if(user.isEmpty()){
           throw new UsernameNotFoundException("User with email + " + username + " not found!");
       }

       return user.get();
    }
}
