package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT p FROM Product p INNER JOIN p.categories c WHERE c.id = :id")
    List<Product> findProductsByCategoryId(Long id);
}
