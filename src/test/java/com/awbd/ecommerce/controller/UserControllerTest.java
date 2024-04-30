package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.service.UserProfileService;
import com.awbd.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    Model model;

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void findById_ShouldReturnUserProfile() throws Exception {
        Long userId = 1L;
        UserDTO user = new UserDTO();
        UserProfileDTO profile = new UserProfileDTO();
        when(userService.findById(userId)).thenReturn(user);
        when(userService.findRoleOfUserByUserId(userId)).thenReturn("USER");
        when(userProfileService.findByUserId(userId)).thenReturn(profile);

        mockMvc.perform(get("/users/profile/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user", "role", "profile"))
                .andExpect(view().name("user-profile"));
    }

    /*
    TODO:
    - test for guest trying to access user pages
     */

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void findAll_ShouldReturnAllUsers() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(), new UserDTO());
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/show-users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", users))
                .andExpect(view().name("user-list"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void deleteById_ShouldRedirectAfterDeletion() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).deleteById(userId);
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showUpdateForm_ShouldReturnUserForm() throws Exception {
        Long userId = 1L;
        UserProfileDTO profile = new UserProfileDTO();
        when(userProfileService.findByUserId(userId)).thenReturn(profile);

        mockMvc.perform(get("/users/edit/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"))
                .andExpect(view().name("user-form"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void saveOrUpdate_ShouldSaveProfileAndRedirect() throws Exception {
        UserProfileDTO profile = new UserProfileDTO();
        profile.setUserId(1L);
        profile.setLastName("test last name");
        profile.setFirstName("test first name");
        profile.setPhoneNumber("0712345678");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("profile", profile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/1"));

        verify(userProfileService).save(profile);
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showRegistrationForm_ShouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-new-user"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void registerUserAccount_ShouldRegisterUserAndRedirect() throws Exception {
        User user = new User();
        user.setUsername("new user");
        user.setPassword("password");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).registerNewUser(user.getUsername(), user.getPassword());
    }

}
