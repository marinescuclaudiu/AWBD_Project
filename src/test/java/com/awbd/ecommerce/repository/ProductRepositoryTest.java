package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@ActiveProfiles("h2")
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testFindAllProducts() {
        List<Product> products = productRepository.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<Product> foundProductOptional = productRepository.findById(1L);
        assertTrue(foundProductOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertTrue(productRepository.existsById(1L));
    }

}
