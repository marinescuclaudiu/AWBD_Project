package com.awbd.ecommerce.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private Long id;
    private String email;
    private String role;
    private String message;
}
