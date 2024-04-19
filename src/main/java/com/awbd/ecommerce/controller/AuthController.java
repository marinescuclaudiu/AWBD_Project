package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.security.LoginRequest;
import com.awbd.ecommerce.dto.security.SignUpRequest;
import com.awbd.ecommerce.service.security.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

//    @PostMapping("/signup")
//    public ResponseEntity<SignUpResponse> signUp(@Valid @ModelAttribute SignUpRequest signUpRequest) {
//        return ResponseEntity.ok(authService.signUp(signUpRequest));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> logIn(@Valid @ModelAttribute LoginRequest loginRequest){
//        return ResponseEntity.ok(authService.login(loginRequest));
//    }

//    -----------------
    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return "redirect:/auth/login";
    }
//    @PostMapping("/login")
//    public String logIn(@Valid @ModelAttribute LoginRequest loginRequest){
//        authService.login(loginRequest);
//        System.out.println(
//                ResponseEntity.ok(authService.login(loginRequest))
//        );
//        return "redirect:/";
//    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jwtToken = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            session.setAttribute("jwtToken", jwtToken);
            return "redirect:/";
        } else {
            return "login";
        }
    }


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
