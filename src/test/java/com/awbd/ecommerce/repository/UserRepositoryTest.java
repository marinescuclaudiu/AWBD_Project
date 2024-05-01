package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

@DataJpaTest
@ActiveProfiles("sql")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Test
    public void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<User> foundUserOptional = userRepository.findById(1L);
        assertFalse(foundUserOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertFalse(userRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        userRepository.deleteById(1L);
        assertFalse(userRepository.existsById(1L));
    }
}
