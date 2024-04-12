package com.awbd.ecommerce.dto;

import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private double rating;
    private String content;
    private LocalDate date;
//    private User user;
}
