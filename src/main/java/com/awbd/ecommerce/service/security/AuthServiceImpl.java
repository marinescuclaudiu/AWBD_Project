package com.awbd.ecommerce.service.security;

import com.awbd.ecommerce.dto.security.*;
import com.awbd.ecommerce.exception.AuthenticationFailedException;
import com.awbd.ecommerce.exception.DatabaseErrorException;
import com.awbd.ecommerce.exception.EmailAlreadyExistsException;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.User;
import com.awbd.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Profile("mysql")
public class AuthServiceImpl implements AuthService{
    private UserRepository userRepository;
    private JWTUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest){
        log.info("Attempting to sign up user with email: {}", signUpRequest.getEmail());

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            log.error("Email {} is already in use", signUpRequest.getEmail());
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        User user = new User(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getRole());
        User savedUser = userRepository.save(user);

        if(savedUser != null){
            log.info("User with email {} signed up successfully", signUpRequest.getEmail());
            return new SignUpResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getRole(), "User saved successfully");
        } else {
            log.error("Failed to save user with email {}", signUpRequest.getEmail());
            throw new DatabaseErrorException("Failed to save user");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting to log in user with email: {}", loginRequest.getEmail());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            log.error("Invalid email or password for user with email: {}", loginRequest.getEmail());
            throw new AuthenticationFailedException("Invalid email or password");
        }

        Optional<User> userOptional = userRepository.findUserByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            log.error("User with email {} not found", loginRequest.getEmail());
            throw new ResourceNotFoundException("User with email " + loginRequest.getEmail() + " not found");
        }

        String jwt = jwtUtils.generateToken(userOptional.get());

        log.info("User with email {} logged in successfully", loginRequest.getEmail());
        return new LoginResponse(
                userOptional.get().getId(),
                userOptional.get().getEmail(),
                userOptional.get().getRole(),
                jwt,
                "8H",
                "Successfully logged in"
        );
    }
}
