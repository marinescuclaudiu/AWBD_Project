package com.awbd.ecommerce.dto;

import com.awbd.ecommerce.model.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    private AddressDTO addressDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate orderDate;

    private List<OrderProductDTO> orderProductDTOS;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float totalAmount;

    @Enumerated(value = EnumType.STRING)
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
