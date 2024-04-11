package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);

    List<CategoryDTO> findAll();

    CategoryDTO findById(Long id);

    void deleteById(Long id);

    CategoryDTO update(Long id, CategoryDTO newCategory);
}
