package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> save(@RequestBody OrderDTO order) {
        return ResponseEntity.ok().body(orderService.save(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> orders = orderService.findAll();
        return ResponseEntity.ok().body(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok().body(orderService.update(id, orderDTO));
    }
}
