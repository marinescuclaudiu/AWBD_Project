package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"","/","/home"})
    public ModelAndView getHome(){
        return new ModelAndView("/auth/main");
    }

    @GetMapping("/login")
    public String showLogInForm() {
        return "/auth/login";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "/exceptions/access-denied"; }
}
