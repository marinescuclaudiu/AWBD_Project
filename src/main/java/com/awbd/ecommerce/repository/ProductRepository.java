package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT r FROM Review r JOIN r.product p WHERE p.id = :id")
    List<Review> getReviewsOfProductByProductId(Long id);
}