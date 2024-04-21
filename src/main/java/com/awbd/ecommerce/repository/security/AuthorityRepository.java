package com.awbd.ecommerce.repository.security;

import com.awbd.ecommerce.model.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByRole(String roleUser);
}
