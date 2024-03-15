package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}