package com.awbd.ecommerce.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
        private Long id;
        private String email;
        private String role;
        private String token;
        private String expirationTime;
        private String message;
}
