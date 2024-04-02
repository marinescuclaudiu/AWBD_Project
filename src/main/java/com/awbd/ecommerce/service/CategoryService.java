package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.repository.CategoryRepository;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);

    List<CategoryDTO> findAll();

    CategoryDTO findById(Long l);

    void deleteById(Long id);

    CategoryDTO update(Long id, CategoryDTO newCategory);
}
