package com.awbd.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;
@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Float price;
    private String color;
    private String photoLink;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "product")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts;


}
