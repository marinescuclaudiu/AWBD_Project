package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> save(@Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok().body(reviewService.save(reviewDTO));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll() {
        List<ReviewDTO> reviews = reviewService.findAll();
        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(reviewService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO){
        return ResponseEntity.ok().body(reviewService.update(id, reviewDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
