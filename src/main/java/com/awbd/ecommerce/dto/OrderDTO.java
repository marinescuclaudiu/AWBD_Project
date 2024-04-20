package com.awbd.ecommerce.dto;

import com.awbd.ecommerce.model.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    @NotNull(message = "Address is required")
    private AddressDTO addressDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate orderDate;

    @NotNull(message = "Order products are required")
    @Size(min = 1, message = "At least one order product is required")
    private List<OrderProductDTO> orderProductDTOS;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float totalAmount;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public OrderDTO(){
        this.orderDate = LocalDate.now();
    }

    public OrderDTO(Long id, Long userId, String email, AddressDTO addressDTO, List<OrderProductDTO> orderProductDTOS, Float totalAmount, PaymentMethod paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.addressDTO = addressDTO;
        this.orderDate = LocalDate.now();
        this.orderProductDTOS = orderProductDTOS;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }
}
