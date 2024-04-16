package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
   private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("admin_user/order")
    public ResponseEntity<OrderDTO> save(@Valid @RequestBody OrderDTO order) {
        return ResponseEntity.ok().body(orderService.save(order));
    }

    @GetMapping("admin_user/order/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @GetMapping("admin/orders")
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<OrderDTO> orders = orderService.findAll();
        return ResponseEntity.ok().body(orders);
    }

    @PatchMapping("admin/order/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok().body(orderService.update(id, orderDTO));
    }

    @DeleteMapping("admin/order/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
