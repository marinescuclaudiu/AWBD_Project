package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> productList() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                productService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO newProduct) {
        return ResponseEntity.ok().body(
                productService.save(newProduct)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}