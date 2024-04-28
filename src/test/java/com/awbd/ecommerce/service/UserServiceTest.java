package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.security.Authority;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.security.AuthorityRepository;
import com.awbd.ecommerce.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void findAll_Success() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> userList = Arrays.asList(user1, user2);

        UserDTO userDto1 = new UserDTO();
        userDto1.setId(1L);
        userDto1.setUsername("user1");

        UserDTO userDto2 = new UserDTO();
        userDto2.setId(2L);
        userDto2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(user1, UserDTO.class)).thenReturn(userDto1);
        when(modelMapper.map(user2, UserDTO.class)).thenReturn(userDto2);

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(userDto1.getId(), result.get(0).getId());
        assertEquals(userDto1.getUsername(), result.get(0).getUsername());
        assertEquals(userDto2.getId(), result.get(1).getId());
        assertEquals(userDto2.getUsername(), result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(user1, UserDTO.class);
        verify(modelMapper, times(1)).map(user2, UserDTO.class);
    }

    @Test
    void findById_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testUser");

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findById(userId)).thenReturn(optionalUser);

        doReturn(userDTO)
                .when(modelMapper)
                .map(optionalUser.get(), UserDTO.class);

        // Act
        UserDTO result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(optionalUser.get(), UserDTO.class);
    }

    @Test
    void findById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(userId);
        });
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void deleteById_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteById(userId);

        // Assert
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteById_UserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId); // Ensure deleteById is never called
    }

    @Test
    void update_Success() {
        // Arrange
        Long userId = 1L;
        String updatedUsername = "updatedUser";
        String updatedPassword = "updatedPassword";

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setUsername(updatedUsername);
        updatedUserDTO.setPassword(updatedPassword);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(updatedUsername);
        updatedUser.setPassword(updatedPassword);

        Optional<User> optionalUser = Optional.of(existingUser);

        when(userRepository.findById(userId)).thenReturn(optionalUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(updatedUserDTO);

        // Act
        UserDTO result = userService.update(userId, updatedUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(updatedUsername, result.getUsername());
        assertEquals(updatedPassword, result.getPassword());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        verify(modelMapper, times(1)).map(any(User.class), eq(UserDTO.class));
        verifyNoMoreInteractions(userRepository, modelMapper);
    }

    @Test
    void update_UserNotFound() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.update(userId, userDTO));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class)); // Ensure save is never called
    }

    @Test
    void findByUsername_Success() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername(username);

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        doReturn(userDTO)
                .when(modelMapper)
                .map(optionalUser.get(), UserDTO.class);

        // Act
        UserDTO result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        verify(modelMapper, times(1)).map(optionalUser.get(), UserDTO.class);
    }

    @Test
    void findByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void registerNewUser_UsernameAlreadyExists() {
        // Arrange
        String existingUsername = "existingUser";
        String password = "password";

        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(new User()));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> userService.registerNewUser(existingUsername, password));

        verify(userRepository, times(1)).findByUsername(existingUsername);
        verifyNoInteractions(passwordEncoder, authorityRepository);
    }

    @Test
    void registerNewUser_Success() {
        // Arrange
        String newUsername = "newUser";
        String password = "password";

        when(userRepository.findByUsername(newUsername)).thenReturn(Optional.empty());
        when(authorityRepository.findByRole("ROLE_GUEST")).thenReturn(new Authority("ROLE_GUEST"));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // Act
        userService.registerNewUser(newUsername, password);

        // Assert
        verify(userRepository, times(1)).findByUsername(newUsername);
        verify(authorityRepository, times(1)).findByRole("ROLE_GUEST");
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findRoleOfUserByUserId_Success() {
        // Arrange
        Long userId = 1L;
        String userRole = "ROLE_USER";

        when(userRepository.findRoleOfUserByUserId(userId)).thenReturn(userRole);

        // Act
        String result = userService.findRoleOfUserByUserId(userId);

        // Assert
        assertEquals(userRole, result);
        verify(userRepository, times(1)).findRoleOfUserByUserId(userId);
    }

}
