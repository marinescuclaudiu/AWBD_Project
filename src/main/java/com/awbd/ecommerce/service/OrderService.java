package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO save(OrderDTO orderDTO);
    List<OrderDTO> findAll();
    OrderDTO findById(Long id);
    void deleteById(Long id);
    OrderDTO update(Long id, OrderDTO orderDTO);
}
