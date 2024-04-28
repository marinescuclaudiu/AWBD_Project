package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc
@Profile("mysql")
public class ReviewControllerTest {
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ProductService productService;

    @MockBean
    Model model;

    @Test
    public void testFindAll() throws Exception {
        when(reviewService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("review-form"))
                .andExpect(model().attributeExists("reviews"));

        verify(reviewService, times(1)).findAll();
    }

    @Test
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
    public void testSaveOrUpdateReview() throws Exception {
        Long productId = 1L;
        ReviewDTO mockReviewDTO = new ReviewDTO();
        mockReviewDTO.setId(1L);
        mockReviewDTO.setContent("Great product");

        mockMvc.perform(post("/products/{productId}/review", productId)
                        .flashAttr("reviewDTO", mockReviewDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + productId));

        verify(reviewService, times(1)).save(mockReviewDTO);
    }

    //TODO: test exceptions
}
