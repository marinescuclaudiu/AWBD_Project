package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@ActiveProfiles("sql")
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testFindAllOrders() {
        List<Order> orders = orderRepository.findAll();
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<Order> foundOrderOptional = orderRepository.findById(1L);
        assertFalse(foundOrderOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertFalse(orderRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        orderRepository.deleteById(1L);
        assertFalse(orderRepository.existsById(1L));
    }
}
