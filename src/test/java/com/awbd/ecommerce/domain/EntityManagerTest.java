package com.awbd.ecommerce.domain;

import com.awbd.ecommerce.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("h2")
public class EntityManagerTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findProduct() {
        System.out.println(entityManager.getEntityManagerFactory());
        Product productFound = entityManager.find(Product.class, 1L);
        assertEquals(productFound.getPrice(), 292);
    }
}
