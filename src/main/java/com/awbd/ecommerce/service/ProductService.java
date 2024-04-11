package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;

import java.util.List;

public interface ProductService {
    ProductDTO save(ProductDTO product);
    List<ProductDTO> findAll();
    ProductDTO findById(Long l);
    void deleteById(Long id);
    ProductDTO update(Long id, ProductDTO productDTO);
    List<ReviewDTO> getReviewsOfProductByProductId(Long id);
}
