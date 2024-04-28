package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.UserProfileRepository;
import com.awbd.ecommerce.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserProfileServiceImpl userProfileService;
    @Mock
    ModelMapper modelMapper;

    @Test
    void save_Success() {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(1L);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserProfile savedUserProfile = new UserProfile();
        when(userProfileRepository.save(any())).thenReturn(savedUserProfile);
        when(modelMapper.map(userProfileDTO, UserProfile.class)).thenReturn(savedUserProfile);
        when(modelMapper.map(savedUserProfile, UserProfileDTO.class)).thenReturn(new UserProfileDTO());

        // Act
        UserProfileDTO result = userProfileService.save(userProfileDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedUserProfile, modelMapper.map(userProfileDTO, UserProfile.class));
        verify(userRepository).findById(1L);
        verify(userProfileRepository).save(any());
        verify(modelMapper).map(savedUserProfile, UserProfileDTO.class);
    }

    @Test
    void save_UserNotFound() {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userProfileService.save(userProfileDTO));

        verify(userRepository).findById(999L);
        verifyNoInteractions(userProfileRepository, modelMapper);
    }

    @Test
    void findAll_Success() {
        // Arrange
        List<UserProfile> userProfiles = Arrays.asList(
                new UserProfile(), new UserProfile());
        when(userProfileRepository.findAll()).thenReturn(userProfiles);

        List<UserProfileDTO> expectedDTOs = Arrays.asList(
                new UserProfileDTO(), new UserProfileDTO());

        // Mock mapping for non-null UserProfile objects
        when(modelMapper.map(ArgumentMatchers.any(UserProfile.class), ArgumentMatchers.eq(UserProfileDTO.class)))
                .thenAnswer(invocation -> {
                    UserProfile source = invocation.getArgument(0);
                    if (source == null) {
                        return null;
                    } else {
                        int index = userProfiles.indexOf(source);
                        return expectedDTOs.get(index);
                    }
                });

        // Act
        List<UserProfileDTO> result = userProfileService.findAll();

        // Assert
        assertEquals(expectedDTOs, result);
        verify(userProfileRepository).findAll();
        verify(modelMapper, times(2)).map(ArgumentMatchers.any(UserProfile.class), ArgumentMatchers.eq(UserProfileDTO.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        long userId = 1L;
        UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(userProfile));

        UserProfileDTO expectedDTO = new UserProfileDTO();
        expectedDTO.setId(userId);
        when(modelMapper.map(userProfile, UserProfileDTO.class)).thenReturn(expectedDTO);

        // Act
        UserProfileDTO result = userProfileService.findById(userId);

        // Assert
        assertEquals(expectedDTO, result);
        verify(userProfileRepository).findById(userId);
        verify(modelMapper).map(userProfile, UserProfileDTO.class);
    }

    @Test
    void findById_UserProfileNotFound() {
        // Arrange
        long nonExistingUserId = 999L;
        when(userProfileRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userProfileService.findById(nonExistingUserId));
        verify(userProfileRepository).findById(nonExistingUserId);
    }

    @Test
    void deleteById_Success() {
        // Arrange
        long userId = 1L;
        when(userProfileRepository.existsById(userId)).thenReturn(true);

        // Act
        userProfileService.deleteById(userId);

        // Assert
        verify(userProfileRepository).existsById(userId);
        verify(userProfileRepository).deleteById(userId);
    }

    @Test
    void deleteById_UserProfileNotFound() {
        // Arrange
        long nonExistingUserId = 999L;
        when(userProfileRepository.existsById(nonExistingUserId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userProfileService.deleteById(nonExistingUserId));
        verify(userProfileRepository).existsById(nonExistingUserId);
    }

    @Test
    void update_Success() {
        // Arrange
        long userId = 1L;
        UserProfile existingUserProfile = new UserProfile();
        existingUserProfile.setId(userId);
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingUserProfile));

        UserProfileDTO updatedUserProfileDTO = new UserProfileDTO();
        updatedUserProfileDTO.setFirstName("UpdatedFirstName");
        updatedUserProfileDTO.setLastName("UpdatedLastName");

        // Act
        UserProfileDTO result = userProfileService.update(userId, updatedUserProfileDTO);

        // Assert
        assertEquals(updatedUserProfileDTO.getFirstName(), existingUserProfile.getFirstName());
        assertEquals(updatedUserProfileDTO.getLastName(), existingUserProfile.getLastName());
        verify(userProfileRepository).findById(userId);
        verify(userProfileRepository).save(existingUserProfile);
    }

    @Test
    void update_UserProfileNotFound() {
        // Arrange
        long nonExistingUserId = 999L;
        when(userProfileRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        UserProfileDTO updatedUserProfileDTO = new UserProfileDTO();
        updatedUserProfileDTO.setFirstName("UpdatedFirstName");
        updatedUserProfileDTO.setLastName("UpdatedLastName");

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userProfileService.update(nonExistingUserId, updatedUserProfileDTO));
        verify(userProfileRepository).findById(nonExistingUserId);
    }

    @Test
    void findByUserId_Success() {
        // Arrange
        long userId = 1L;
        User user = new User();
        user.setId(userId);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setFirstName("No first name");
        userProfile.setLastName("No last name");
        userProfile.setPhoneNumber("No phone number");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(userId);
        userProfileDTO.setFirstName(userProfile.getFirstName());
        userProfileDTO.setLastName(userProfile.getLastName());
        userProfileDTO.setPhoneNumber(userProfile.getPhoneNumber());

        when(modelMapper.map(userProfile, UserProfileDTO.class)).thenReturn(userProfileDTO);

        // Act
        UserProfileDTO result = userProfileService.findByUserId(userId);

        // Assert
        verify(userRepository).findById(userId);
        assertEquals(userProfile.getFirstName(), result.getFirstName());
        assertEquals(userProfile.getLastName(), result.getLastName());
        assertEquals(userProfile.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void findByUserId_UserProfileNotFound() {
        // Arrange
        long nonExistingUserId = 999L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userProfileService.findByUserId(nonExistingUserId));
        verify(userRepository).findById(nonExistingUserId);
    }
}
