package com.awbd.ecommerce.model;

import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.model.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating is required")
    @Positive(message = "Rating must be positive")
    private double rating;

    private String content;

    @FutureOrPresent(message = "The date must be in the present or in the past")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review(Long id, double rating, String content) {
        this.id = id;
        this.rating = rating;
        this.content = content;
    }
}
