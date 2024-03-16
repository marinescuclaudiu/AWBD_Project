package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Assertions;
import java.util.Optional;

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
        newUser.setEmail("kyla_courtney@gmail.com");

        UserProfile profile = new UserProfile();
        profile.setFirstName("Kyla");
        profile.setLastName("Courtney");
        profile.setPhoneNumber("0128279478");

        // act
        newUser.setUserProfile(profile);
        userRepository.save(newUser);

        Optional<User> userOpt = userRepository.findByEmail("kyla_courtney@gmail.com");
        User savedUser = userOpt.get();

        // assert
        Assertions.assertEquals("Kyla", savedUser.getUserProfile().getFirstName());
        Assertions.assertEquals("Courtney", savedUser.getUserProfile().getLastName());
    }

    @Test
    public void updateUser() {
        // arrange
        User theUser = userRepository.findById(1L).get();
        theUser.getUserProfile().setPhoneNumber("0122279478");

        userRepository.save(theUser);

        // act
        User changedUser = userRepository.findById(1L).get();

        // assert
        Assertions.assertEquals("0122279478", changedUser.getUserProfile().getPhoneNumber());
    }
}
