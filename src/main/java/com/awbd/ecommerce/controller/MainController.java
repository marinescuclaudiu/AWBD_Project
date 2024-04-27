package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ModelAndView getHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            UserDTO userDTO = userService.findByUsername(username);

            model.addAttribute("loggedUserId", userDTO.getId());
            model.addAttribute("username", username);
        } else {
            model.addAttribute("loggedUserId", null);
            model.addAttribute("username", null);
        }

        return new ModelAndView("/auth/main");
    }

    @GetMapping("/login")
    public String showLogInForm() {
        return "/auth/login";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "/exceptions/access-denied"; }

}
