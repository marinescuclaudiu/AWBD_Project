package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
}
