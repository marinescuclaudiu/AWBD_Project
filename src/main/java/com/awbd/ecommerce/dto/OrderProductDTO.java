package com.awbd.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {
    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    private Integer quantity;
}
