package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.ReviewService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ProductService productService;

    @MockBean
    Model model;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testShowReviews() throws Exception {
        Long productId = 1L;
        when(productService.findById(productId)).thenReturn(new ProductDTO());

        mockMvc.perform(get("/products/{productId}/reviewForm", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("review-form"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("review"));

        verify(productService, times(1)).findById(productId);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testDeleteById() throws Exception {
        Long reviewId = 1L;
        Long productId = 123L;

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(productId);

        when(reviewService.findById(reviewId)).thenReturn(reviewDTO);

        mockMvc.perform(get("/reviews/delete/{id}", reviewId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + productId));

        verify(reviewService, times(1)).deleteById(reviewId);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testEditReviewForm() throws Exception {
        Long reviewId = 1L;
        Long productId = 123L;

        ReviewDTO mockReviewDTO = new ReviewDTO();
        mockReviewDTO.setProductId(productId);

        when(reviewService.findById(reviewId)).thenReturn(mockReviewDTO);
        when(productService.findById(productId)).thenReturn(new ProductDTO());

        mockMvc.perform(get("/reviews/edit/{reviewId}", reviewId))
                .andExpect(status().isOk())
                .andExpect(view().name("review-form"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("review"));

        verify(reviewService, times(1)).findById(reviewId);
        verify(productService, times(1)).findById(productId);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testSaveOrUpdateReview() throws Exception {
        Long productId = 1L;
        ReviewDTO review = new ReviewDTO();
        review.setId(5L);
        review.setContent("Great product!");

        when(reviewService.save(any(ReviewDTO.class))).thenReturn(review);

        mockMvc.perform(post("/products/{productId}/review", productId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("reviewDTO", review)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + productId));

        verify(reviewService, times(1)).save(review);
    }
}
