package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.repository.UserProfileRepository;
import com.awbd.ecommerce.repository.security.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService{
    UserProfileRepository userProfileRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.info("Saving user profile for user with id: {}", userProfileDTO.getUserId());
        Optional<User> user = userRepository.findById(userProfileDTO.getUserId());

        if(user.isEmpty()){
            log.error("User with id {} not found!", userProfileDTO.getUserId());
            throw new ResourceNotFoundException("User with id " + userProfileDTO.getUserId() + " not found!");
        }

        UserProfile savedUserProfile = userProfileRepository.save(modelMapper.map(userProfileDTO, UserProfile.class));
        user.get().setUserProfile(savedUserProfile);

        log.info("User profile saved for user with id: {}", user.get().getId());
        return modelMapper.map(savedUserProfile, UserProfileDTO.class);
    }

    @Override
    public List<UserProfileDTO> findAll() {
        log.info("Fetching all user profiles");
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        log.info("Found {} user profiles", userProfiles.size());
        return userProfiles.stream()
                .map(userProfile -> modelMapper.map(userProfile, UserProfileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDTO findById(Long id) {
        log.info("Fetching user profile by ID: {}", id);
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);

        if(userProfile.isEmpty()){
            log.error("User profile with id {} not found!", id);
            throw new ResourceNotFoundException("User profile with id " + id + " not found!");
        }

        log.info("User profile with id {} found", userProfile.get().getId());
        return modelMapper.map(userProfile.get(), UserProfileDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting user profile by ID: {}", id);

        if (!userProfileRepository.existsById(id)) {
            log.error("User profile with id {} not found", id);
            throw new ResourceNotFoundException("User profile with id " + id + " not found!");
        }

        userProfileRepository.deleteById(id);
        log.info("Category deleted successfully");
    }

    @Transactional
    @Override
    public UserProfileDTO update(Long id, UserProfileDTO userProfileDTO) {
        log.info("Updating user profile with ID: {}", id);
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);

        if(userProfile.isEmpty()){
            log.error("User profile with id {} not found", id);
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }

        BeanUtils.copyProperties(userProfileDTO, userProfile.get(), BeanHelper.getNullPropertyNames(userProfileDTO));

        userProfileRepository.save(userProfile.get());

        log.info("User profile with id {} updated", userProfile.get().getId());
        return modelMapper.map(userProfile.get(), UserProfileDTO.class);
    }

    @Override
    public UserProfileDTO findByUserId(Long userId) {
        log.info("Fetching user profile by user ID: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            log.error("User with id {} not found", userId);
            throw new ResourceNotFoundException("User with id " + userId + " not found!");
        }

        UserProfile userProfile = userOptional.get().getUserProfile();

        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUser(userOptional.get());
            userProfile.setFirstName("No first name");
            userProfile.setLastName("No last name");
            userProfile.setPhoneNumber("No phone number");
        }

        log.info("User profile with user ID {} found", userId);
        return modelMapper.map(userProfile, UserProfileDTO.class);
    }
}
