package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.service.UserProfileService;
import com.awbd.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    UserService userService;
    UserProfileService userProfileService;

    @Autowired
    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @RequestMapping("/users/profile/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findById(id);
        String role = userService.findRoleOfUserByUserId(id);
        UserProfileDTO userProfileDTO = userProfileService.findByUserId(id);

        model.addAttribute("user", userDTO);
        model.addAttribute("role", role);
        model.addAttribute("profile", userProfileDTO);

        return "user-profile";
    }

    @GetMapping("/show-users")
    public String findAll(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @RequestMapping("/users/{id}")
    public String deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @RequestMapping("/users/edit/{id}")
    public String update(@PathVariable Long id, Model model) {
        UserProfileDTO userProfileDTO = userProfileService.findByUserId(id);
        model.addAttribute("profile", userProfileDTO);

        return "user-form";
    }

    @PostMapping("/users")
    public String saveOrUpdate(@ModelAttribute UserProfileDTO userProfileDTO) {
        System.out.println("----user profile DTO");
        System.out.println(userProfileDTO);

        userProfileService.save(userProfileDTO);
        return "redirect:/users/profile/" + userProfileDTO.getUserId();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user) {
        userService.registerNewUser(user.getUsername(), user.getPassword());
        return "redirect:/login";
    }
}
