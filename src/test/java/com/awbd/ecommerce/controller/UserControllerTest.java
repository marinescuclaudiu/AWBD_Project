package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.config.SecurityJpaConfig;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.service.UserProfileService;
import com.awbd.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Profile("mysql")
@Import(SecurityJpaConfig.class)
public class UserControllerTest {
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    UserService userService;

    @MockBean
    UserProfileService userProfileService;

    @MockBean
    Model model;

    @MockBean
    private BindingResult bindingResult;

    @Test
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

    @Test
    public void findAll_ShouldReturnAllUsers() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(), new UserDTO());
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/show-users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", users))
                .andExpect(view().name("user-list"));
    }

    @Test
    public void deleteById_ShouldRedirectAfterDeletion() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).deleteById(userId);
    }

    @Test
    public void update_ShouldReturnUserForm() throws Exception {
        Long userId = 1L;
        UserProfileDTO profile = new UserProfileDTO();
        when(userProfileService.findByUserId(userId)).thenReturn(profile);

        mockMvc.perform(get("/users/edit/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"))
                .andExpect(view().name("user-form"));
    }

    @Test
    public void saveOrUpdate_ShouldSaveProfileAndRedirect() throws Exception {
        UserProfileDTO profile = new UserProfileDTO();
        profile.setUserId(1L);

        mockMvc.perform(post("/users")
                        .flashAttr("profile", profile))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/1"));

        verify(userProfileService).save(profile);
    }

    @Test
    public void showRegistrationForm_ShouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-new-user"));
    }

    @Test
    public void registerUserAccount_ShouldRegisterUserAndRedirect() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password");

        mockMvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).registerNewUser(user.getUsername(), user.getPassword());
    }

}
