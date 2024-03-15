package com.awbd.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String district;

    @OneToMany(mappedBy = "address")
    private List<Order> orders;
}
