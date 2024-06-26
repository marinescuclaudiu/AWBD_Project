package com.awbd.ecommerce.config;

import com.awbd.ecommerce.service.security.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("mysql")
public class SecurityJpaConfig {

    private final JpaUserDetailsService userDetailsService;

    public SecurityJpaConfig(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(auth -> auth
                        .requestMatchers("/products/form").hasRole("ADMIN")
                        .requestMatchers("/categories/form").hasRole("ADMIN")
                        .requestMatchers("/", "/webjars/**", "/login", "/resources/**", "main", "register").permitAll()
                        .requestMatchers("/show-users").hasRole("ADMIN")
                        .requestMatchers("/products/delete/*").hasRole("ADMIN")
                        .requestMatchers("/products/edit/*").hasRole("ADMIN")
                        .requestMatchers("/categories/edit/*").hasAnyRole("ADMIN")
                        .requestMatchers("/categories/delete/*").hasAnyRole("ADMIN")
                        .requestMatchers("/products/*").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/categories/*").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/users/*").hasAnyRole("ADMIN", "GUEST")
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .loginProcessingUrl("/perform_login")
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access_denied"))
                .build();
    }
}
