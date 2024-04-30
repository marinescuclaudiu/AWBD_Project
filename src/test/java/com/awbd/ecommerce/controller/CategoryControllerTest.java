package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    ProductService productService;

    @MockBean
    Model model;

    @MockBean
    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findAll() throws Exception {
        CategoryDTO category1 = new CategoryDTO(); category1.setName("category1");
        CategoryDTO category2 = new CategoryDTO(); category2.setName("category2");

        List<CategoryDTO> categories = Arrays.asList(category1, category2);

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("category-list"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", categories));

        verify(categoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findById() throws Exception {
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);

        when(categoryService.findById(categoryId)).thenReturn(categoryDTO);

        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("category-details"))
                .andExpect(model().attribute("category", categoryDTO));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findById_numberFormatException() throws Exception {
        // arrange & act
        when(categoryService.findById(-1L)).thenThrow(ResourceNotFoundException.class);

        // assert
        mockMvc.perform(get("/categories/{id}", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("numberFormatException"));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findById_notFoundException() throws Exception {
        Long id = -1L;

        when(categoryService.findById(id)).thenThrow(new ResourceNotFoundException("Category not found"));

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("notFoundException"))
                .andExpect(model().attributeExists("exception"));

        verify(categoryService, times(1)).findById(id);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void showCategoryFormToUser() throws Exception {
        mockMvc.perform(get("/categories/form"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showCategoryFormToAdmin() throws Exception {
        mockMvc.perform(get("/categories/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("category-form"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void testSaveOrUpdate_WithValidCategory_ShouldSaveCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");

        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/categories")
                        .with(csrf())
                        .param("name", "Test Category")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService, times(1)).save(categoryDTO);
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void deleteById_WhenCalled_ShouldRedirect() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/categories/delete/{id}", categoryId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService, times(1)).deleteById(categoryId);
    }
}
