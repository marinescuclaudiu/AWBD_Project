package com.awbd.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReviewDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Positive(message = "Rating must be positive")
    @Size(min = 1, max = 5, message = "Rating must be between 1 and 5")
    private int rating;

    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    public ReviewDTO(){
        this.date = LocalDate.now();
    }
}
