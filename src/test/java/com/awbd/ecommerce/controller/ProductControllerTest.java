package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.config.SecurityJpaConfig;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
@Profile("mysql")
@Import(SecurityJpaConfig.class)
public class ProductControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    Model model;

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void editById() throws Exception {

        Long id = 1L;
        ProductDTO productTestDTO = new ProductDTO();
        productTestDTO.setId(id);

        when(productService.findById(id)).thenReturn(productTestDTO);

        mockMvc.perform(get("/products/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attribute("product", productTestDTO));
    }

//    @Test
//    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
//    public void showProductFormToGuest() throws Exception {
//        mockMvc.perform(get("/products/form"))
//                .andExpect(status().isForbidden())
//                .andExpect(view().name("accessDenied"));
//    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showProductFormToAdmin() throws Exception {

        mockMvc.perform(get("/products/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void getImage() throws Exception {
        Long id = 1L;
        ProductDTO productTestDTO = new ProductDTO();
        productTestDTO.setId(id);

        byte[] imageBytes = { 0x12, 0x34, 0x56, 0x78};
        productTestDTO.setPhoto(imageBytes);

        when(productService.findById(id)).thenReturn(productTestDTO);

        mockMvc.perform(get("/products/getimage/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void testSaveOrUpdate_WithValidProductAndNoFile_ShouldSaveProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("Test Product");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products").file("imagefile", new byte[0])
                        .with(csrf())
                        .param("name", "Test Product")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        ArgumentCaptor<ProductDTO> argumentCaptor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService, times(1))
                .save(argumentCaptor.capture() );

        ProductDTO productArg = argumentCaptor.getValue();
        assertEquals(productArg.getName(), product.getName() );
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showByIdNotFound() throws Exception {
        Long id = 1L;

        when(productService.findById(id)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/products/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("defaultException"));
    }
}
