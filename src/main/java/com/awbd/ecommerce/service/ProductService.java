package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDTO save(ProductDTO product);
    List<ProductDTO> findAll();
    ProductDTO findById(Long l);
    void deleteById(Long id);
    ProductDTO update(Long id, ProductDTO newProduct);
}
