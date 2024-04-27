package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void givenUserAndUserProfile_saveUserAndUserProfile() {
        // arrange
        User newUser = new User();
        newUser.setUsername("kyla_courtney");
        newUser.setPassword("12345");

        UserProfile profile = new UserProfile();
        profile.setFirstName("Kyla");
        profile.setLastName("Courtney");
        profile.setPhoneNumber("0128279478");

        // act
        newUser.setUserProfile(profile);
        userRepository.save(newUser);

        Optional<User> userOpt = userRepository.findByUsername("kyla_courtney");
        User savedUser = userOpt.get();

        // assert
        assertEquals("Kyla", savedUser.getUserProfile().getFirstName());
        assertEquals("Courtney", savedUser.getUserProfile().getLastName());
    }

//    @Test
//    public void updateUser() {
//        // arrange
//        User theUser = new User("anonymous", "12345");
//        theUser.setId(1L);
//        userRepository.save(theUser);
//
//        User foundUser = userRepository.findById(1L).get();
//        foundUser.getUserProfile().setPhoneNumber("0122279478");
//
//        userRepository.save(foundUser);
//
//        // act
//        User changedUser = userRepository.findById(1L).get();
//
//        // assert
//        assertEquals("0122279478", changedUser.getUserProfile().getPhoneNumber());
//    }
//
//    @Test
//    public void findAll() {
//        // arrange
//
//        // act
//        List<User> users = userRepository.findAll();
//
//        // assert
//        assertEquals(21, users.size());
//    }
}
