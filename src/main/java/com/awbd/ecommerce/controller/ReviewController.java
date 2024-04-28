package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    ReviewService reviewService;
    ProductService productService;

    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/reviews")
    public String findAll(Model model) {
        List<ReviewDTO> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "review-form";
    }

    @RequestMapping("/products/{productId}/reviewForm")
    public String showReviews(@PathVariable Long productId, Model model) {
        try {
            ProductDTO productDTO = productService.findById(productId);

            model.addAttribute("product", productDTO);
            model.addAttribute("review", new ReviewDTO());

            return "review-form";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @RequestMapping("/reviews/edit/{reviewId}")
    public String editReviewForm(@PathVariable Long reviewId, Model model) {
        ReviewDTO reviewDTO = reviewService.findById(reviewId);
        ProductDTO productDTO = productService.findById(reviewDTO.getProductId());

        model.addAttribute("product", productDTO);
        model.addAttribute("review", reviewDTO);

        return "review-form";
    }

    @PostMapping("/products/{productId}/review")
    public String saveOrUpdateReview(@ModelAttribute ReviewDTO reviewDTO,
                                     @PathVariable Long productId) {
        reviewService.save(reviewDTO);
        return "redirect:/products/" + productId;
    }

    @RequestMapping("/reviews/delete/{id}")
    public String deleteById(@PathVariable Long id, Model model) {
        try {
            Long productId = reviewService.findById(id).getProductId();

            reviewService.deleteById(id);
            return "redirect:/products/" + productId;
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }
}
