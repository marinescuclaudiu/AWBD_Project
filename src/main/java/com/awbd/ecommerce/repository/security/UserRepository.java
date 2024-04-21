package com.awbd.ecommerce.repository.security;

import com.awbd.ecommerce.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    @Query("SELECT a.role FROM User u JOIN u.authorities a WHERE u.id = :userId")
    String findRoleOfUserByUserId(Long userId);
}