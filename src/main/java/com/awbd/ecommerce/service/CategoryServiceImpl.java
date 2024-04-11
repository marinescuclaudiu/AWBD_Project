package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.UserProfile;
import com.awbd.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category savedCategory = categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(
                category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Long l) {
        Optional<Category> categoryOptional = categoryRepository.findById(l);
        if (categoryOptional.isEmpty()) {
            throw new RuntimeException("Category not found!");
        }
        return modelMapper.map(categoryOptional.get(), CategoryDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category with id " + id + " doesn't exists"));

        BeanUtils.copyProperties(categoryDTO, category, BeanHelper.getNullPropertyNames(categoryDTO));

        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }
}
