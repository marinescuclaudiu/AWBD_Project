package com.awbd.ecommerce.service.security;

import com.awbd.ecommerce.dto.security.LoginRequest;
import com.awbd.ecommerce.dto.security.LoginResponse;
import com.awbd.ecommerce.dto.security.SignUpRequest;
import com.awbd.ecommerce.dto.security.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    LoginResponse login(LoginRequest loginRequest);
}
