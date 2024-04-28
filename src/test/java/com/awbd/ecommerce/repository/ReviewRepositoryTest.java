package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@ActiveProfiles("sql")
public class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    public void testFindAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<Review> foundReviewOptional = reviewRepository.findById(1L);
        assertFalse(foundReviewOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertFalse(reviewRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        reviewRepository.deleteById(1L);
        assertFalse(reviewRepository.existsById(1L));
    }
}
