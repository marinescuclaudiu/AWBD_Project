package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.service.UserProfileService;
import com.awbd.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO currentLoggedUser = userService.findByUsername(username);

        if (!Objects.equals(currentLoggedUser.getId(), id)) {
            return "accessDenied";
        }

        try {
            UserDTO userDTO = userService.findById(id);
            String role = userService.findRoleOfUserByUserId(id);
            UserProfileDTO userProfileDTO = userProfileService.findByUserId(id);

            model.addAttribute("user", userDTO);
            model.addAttribute("role", role);
            model.addAttribute("profile", userProfileDTO);

            return "user-profile";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @GetMapping("/show-users")
    public String findAll(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);

        return "user-list";
    }

    @RequestMapping("/users/{id}")
    public String deleteById(@PathVariable Long id, Model model) {
        try {
            userService.deleteById(id);
            return "redirect:/users";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @RequestMapping("/users/edit/{id}")
    public String update(@PathVariable Long id, Model model) {
        try {
            UserProfileDTO userProfileDTO = userProfileService.findByUserId(id);
            model.addAttribute("profile", userProfileDTO);
            return "user-form";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @PostMapping("/users")
    public String saveOrUpdate(@Valid @ModelAttribute("profile") UserProfileDTO userProfileDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("profile", userProfileDTO);
            return "user-form";
        }

        userProfileService.save(userProfileDTO);
        return "redirect:/users/profile/" + userProfileDTO.getUserId();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register-new-user";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register-new-user";
        }

        userService.registerNewUser(user.getUsername(), user.getPassword());
        return "redirect:/login";
    }
}
