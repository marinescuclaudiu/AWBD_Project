package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT r FROM Review r INNER JOIN r.product p WHERE p.id = :id")
    List<Review> getReviewsOfProductByProductId(Long id);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :id")
    double getAverageRatingByProductId(Long id);

    @Query("SELECT p from Product p JOIN p.categories c WHERE c.name = :categoryName ORDER BY p.price")
    List<Product> findByCategoryAndSortByPrice(String categoryName);

    @Query("SELECT p from Product p ORDER BY p.price")
    List<Product> sortByPrice();
}