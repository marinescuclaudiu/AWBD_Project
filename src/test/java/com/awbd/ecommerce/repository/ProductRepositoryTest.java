package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Assertions;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void deleteProductButNotProductReviews() {
        // arrange
        Product productToBeDeleted = new Product();

        // create 2 reviews for this product
        Review review1 = new Review();
        review1.setProduct(productToBeDeleted);

        Review review2 = new Review();
        review2.setProduct(productToBeDeleted);

        // act
        productRepository.delete(productToBeDeleted);

        // assert
        Assertions.assertNotNull(review1);
    }
}
