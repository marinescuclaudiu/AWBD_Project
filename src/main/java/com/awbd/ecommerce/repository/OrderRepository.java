package com.awbd.ecommerce.repository;

import com.awbd.ecommerce.dto.AddressDTO;
import com.awbd.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.address FROM Order o WHERE o.id = :orderId")
    AddressDTO findAddressByOrderId(Long orderId);
}