package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.model.security.Authority;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.security.AuthorityRepository;
import com.awbd.ecommerce.repository.security.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO.getUsername());
        User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));

        log.info("User saved: {}", userDTO.getUsername());
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
    public UserDTO findByUsername(String username) {
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found!"));

        return modelMapper.map(userFound, UserDTO.class);
    }

    @Override
    public void registerNewUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Authority guestRole = authorityRepository.findByRole("ROLE_GUEST");

        // create an empty profile
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("No first name");
        userProfile.setLastName("No last name");
        userProfile.setPhoneNumber("0123456789");

        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authority(guestRole)
                .userProfile(userProfile)
                .build();

        userProfile.setUser(newUser);
        userRepository.save(newUser);
    }

    @Override
    public String findRoleOfUserByUserId(Long userId) {
        return userRepository.findRoleOfUserByUserId(userId);
    }
}
