package com.awbd.ecommerce.dto;

import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Float price;
    private String color;
    private byte[] photo;
}
