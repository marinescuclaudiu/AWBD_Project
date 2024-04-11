package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.User;
import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.repository.UserProfileRepository;
import com.awbd.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService{

    UserProfileRepository userProfileRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        User user = userRepository.findById(userProfileDTO.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User with id " + userProfileDTO.getUserId() + " not found!"));

        UserProfile savedUserProfile = userProfileRepository.save(modelMapper.map(userProfileDTO, UserProfile.class));
        user.setUserProfile(savedUserProfile);

        return modelMapper.map(savedUserProfile, UserProfileDTO.class);
    }

    @Override
    public List<UserProfileDTO> findAll() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        return userProfiles.stream()
                .map(userProfile -> modelMapper.map(userProfile, UserProfileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDTO findById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User profile with id " + id + " not found!"));

        return modelMapper.map(userProfile, UserProfileDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        if (!userProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("User profile with id " + id + " not found!");
        }

        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfileDTO update(Long id, UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("User with id " + id + " not found!")));

        BeanUtils.copyProperties(userProfileDTO, userProfile, BeanHelper.getNullPropertyNames(userProfileDTO));

        userProfileRepository.save(userProfile);

        return modelMapper.map(userProfile, UserProfileDTO.class);
    }
}
