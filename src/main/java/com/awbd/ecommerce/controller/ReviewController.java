package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> findAll() {
        List<ReviewDTO> reviews = reviewService.findAll();
        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> findBYId(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                reviewService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> save(@RequestBody ReviewDTO newReview) {
        return ResponseEntity.ok().body(
                reviewService.save(newReview)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(@RequestBody ReviewDTO reviewDTO,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok().body(
                reviewService.update(id, reviewDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
