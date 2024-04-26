package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.ReviewService;
import com.awbd.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReviewController {

    ReviewService reviewService;
    ProductService productService;
    ModelMapper modelMapper;
    UserService userService;

    public ReviewController(ReviewService reviewService, ProductService productService, ModelMapper modelMapper, UserService userService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/reviews")
    public String findAll(Model model) {
        List<ReviewDTO> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "products/review-form";
    }

    @RequestMapping("/products/{productId}/review-form")
    public String showReviews(@PathVariable Long productId, Model model) {
        ProductDTO productDTO = productService.findById(productId);

        model.addAttribute("product", productDTO);
        model.addAttribute("review", new ReviewDTO());

        return "products/review-form"; // Assuming you have a template named "reviews.html"
    }

    @RequestMapping("/reviews/edit/{reviewId}")
    public String editReviewForm(@PathVariable Long reviewId, Model model) {
        ReviewDTO reviewDTO = reviewService.findById(reviewId);
        ProductDTO productDTO = productService.findById(reviewDTO.getProductId());

        model.addAttribute("product", productDTO);
        model.addAttribute("review", reviewDTO);

        return "products/review-form"; // Assuming you have a template named "reviews.html"
    }

    @PostMapping("/products/{productId}/review")
    public String saveOrUpdateReview(@ModelAttribute ReviewDTO reviewDTO,
                                     @PathVariable Long productId) {
        reviewDTO.setUserId(getCurrentUserId());
        reviewService.save(reviewDTO);

        return "redirect:/products/" + productId;
    }

    @RequestMapping("/reviews/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/products/{id}";
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserDTO userDTO = userService.findByUsername(username);

        return userDTO.getId();
    }
}
