package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    Model model;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void getProductPage_byGuest() throws Exception {
        ProductDTO product1 = new ProductDTO();
        product1.setName("dummy product");
        ProductDTO product2 = new ProductDTO();
        product2.setName("dummy product");

        Page<ProductDTO> page = new PageImpl<>(Arrays.asList(product1, product2), PageRequest.of(0, 10), 0);
        when(productService.findPaginated(any(), isNull())).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-list"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attribute("productPage", page));

        verify(productService, times(1)).findPaginated(PageRequest.of(0, 10), null);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findProductByProductId_existingProduct_byGuest() throws Exception {
        Long id = 1L;
        ProductDTO product = new ProductDTO();
        product.setId(id);
        product.setReviews(new ArrayList<>());

        when(productService.findById(1L)).thenReturn(product);
        when(productService.getAverageRatingByProductId(1L)).thenReturn(0d);

        mockMvc.perform(get("/products/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-details"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attributeExists("averageRating"))
                .andExpect(model().attribute("averageRating", 0d));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findProductByProductId_numberFormatException_byGuest() throws Exception {
        when(productService.findById(123L)).thenThrow(new NumberFormatException());

        mockMvc.perform(get("/products/abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("numberFormatException"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void deleteById_ValidId_RedirectsToProducts() throws Exception {
        Long productId = 1L;

        mockMvc.perform(get("/products/delete/{id}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).deleteById(productId);
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void deleteById_InvalidId_ReturnsNotFound() throws Exception {
        Long invalidId = 999L;
        doThrow(ResourceNotFoundException.class).when(productService).deleteById(invalidId);

        mockMvc.perform(get("/products/delete/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"));

        verify(productService).deleteById(invalidId);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void deleteById_byGuest_throwsException() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/products/delete/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findProductByProductId_nonExistingProduct_throwsException_byGuest() throws Exception {
        Long id = 1L;
        when(productService.findById(id)).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"))
                .andExpect(model().attributeExists("exception"));

        verify(productService, never()).getAverageRatingByProductId(any());
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void editProductByProductId_byGuest_accessDenied() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/products/edit/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void editProductByProductId() throws Exception {
        Long id = 1L;
        ProductDTO productTestDTO = new ProductDTO();
        productTestDTO.setId(id);

        when(productService.findById(id)).thenReturn(productTestDTO);

        mockMvc.perform(get("/products/edit/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attribute("product", productTestDTO));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void showProductFormToGuest() throws Exception {
        mockMvc.perform(get("/products/form"))
                .andExpect(status().isForbidden());
    }

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

        byte[] imageBytes = {0x12, 0x34, 0x56, 0x78};
        productTestDTO.setPhoto(imageBytes);

        when(productService.findById(id)).thenReturn(productTestDTO);

        mockMvc.perform(get("/products/getimage/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void showByIdNotFound() throws Exception {
        Long id = 1L;

        when(productService.findById(id)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/products/edit/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"))
                .andExpect(model().attributeExists("exception"));
    }

    @Test
    @WithMockUser(username = "admin", password = "12345", roles = "ADMIN")
    public void testSaveOrUpdate_WithValidProductAndNoFile_ShouldSaveProduct() throws Exception {
        ProductDTO product = new ProductDTO();

        product.setName("test product");
        product.setDescription("test description");
        product.setPrice(10.99f);
        product.setColor("white");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products").file("imagefile", new byte[0])
                        .with(csrf())
                        .param("name", "test product")
                        .param("description", "test description")
                        .param("price", "10.99f")
                        .param("color", "white")
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
}
