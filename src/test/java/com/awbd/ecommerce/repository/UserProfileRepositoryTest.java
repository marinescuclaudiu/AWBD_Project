package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@ActiveProfiles("sql")
public class UserProfileRepositoryTest {
    @Autowired
    UserProfileRepository userProfileRepository;

    @Test
    public void testFindAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertNotNull(userProfiles);
        assertTrue(userProfiles.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<UserProfile> foundProfilesOptional = userProfileRepository.findById(1L);
        assertFalse(foundProfilesOptional.isPresent());
    }

    @Test
    public void testExistsById() {
        assertFalse(userProfileRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        userProfileRepository.deleteById(1L);
        assertFalse(userProfileRepository.existsById(1L));
    }
}
