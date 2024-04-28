package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    void deleteById(Long id);
    CategoryDTO update(Long id, CategoryDTO newCategory);
    List<ProductDTO> findProductsByCategoryId(Long id);
}
