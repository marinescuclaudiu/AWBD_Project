package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@ActiveProfiles("h2")
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testFindAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<Category> foundCategoryOptional = categoryRepository.findById(1L);
        assertTrue(foundCategoryOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertTrue(categoryRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        categoryRepository.deleteById(1L);
        assertFalse(categoryRepository.existsById(1L));
    }
}
