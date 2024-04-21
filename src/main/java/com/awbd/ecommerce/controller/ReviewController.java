package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReviewController {

    ReviewService reviewService;
    ProductService productService;
    ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ProductService productService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/reviews")
    public String findAll(Model model) {
        List<ReviewDTO> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "review-form";
    }

    @RequestMapping("/products/{productId}/review-form")
    public String showReviews(@PathVariable Long productId, Model model) {
        ProductDTO productDTO = productService.findById(productId);

        model.addAttribute("product", productDTO);
        model.addAttribute("review", new ReviewDTO());

        return "review-form"; // Assuming you have a template named "reviews.html"
    }

    @RequestMapping("/reviews/edit/{reviewId}")
    public String editReviewForm(@PathVariable Long reviewId, Model model) {
        ReviewDTO reviewDTO = reviewService.findById(reviewId);
        ProductDTO productDTO = productService.findById(reviewDTO.getProductId());

        model.addAttribute("product", productDTO);
        model.addAttribute("review", reviewDTO);

        return "review-form"; // Assuming you have a template named "reviews.html"
    }

    @PostMapping("/products/{productId}/review")
    public String saveOrUpdateReview(@ModelAttribute ReviewDTO reviewDTO, @PathVariable Long productId) {
        reviewDTO.setProductId(productId);
        reviewDTO.setDate(LocalDate.now());
        reviewService.save(reviewDTO);
        return "redirect:/products/" + productId;
    }

    @RequestMapping("/reviews/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/products/{id}";
    }
}
