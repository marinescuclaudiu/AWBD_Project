package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.security.LoginRequest;
import com.awbd.ecommerce.dto.security.LoginResponse;
import com.awbd.ecommerce.dto.security.SignUpRequest;
import com.awbd.ecommerce.dto.security.SignUpResponse;
import com.awbd.ecommerce.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @ModelAttribute SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@Valid @ModelAttribute LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

//    -----------------

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "register";
    }

    @GetMapping("/login")
    public String logIn(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
}
