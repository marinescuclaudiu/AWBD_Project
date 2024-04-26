package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.info("Saving category: {}", categoryDTO.getName());
        Category savedCategory = categoryRepository.save(modelMapper.map(categoryDTO, Category.class));

        log.info("Category saved: {}", savedCategory.getName());
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();

        log.info("Found {} categories", categories.size());
        return categories.stream().map(
                category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Long id) {
        log.info("Fetching category by ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isEmpty()){
            log.error("Category with id {} not found!", id);
            throw  new ResourceNotFoundException("Category with " + id + " not found!");
        }

        log.info("Category found: {}", category.get().getName());
        return modelMapper.map(category.get(), CategoryDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting category by ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            log.error("Category with id {} not found", id);
            throw new ResourceNotFoundException("Category with id " + id + " not found!");
        }

        categoryRepository.deleteById(id);
        log.info("Category deleted successfully");
    }

    @Transactional
    @Override
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isEmpty()){
            log.error("Category with id {} not found", id);
            throw new ResourceNotFoundException("Category with id " + id + " not found!");
        }

        BeanUtils.copyProperties(categoryDTO, category.get(), BeanHelper.getNullPropertyNames(categoryDTO));

        categoryRepository.save(category.get());

        log.info("Category updated: {}", category.get().getName());
        return modelMapper.map(category.get(), CategoryDTO.class);
    }

    @Override
    public List<ProductDTO> findProductsByCategoryId(Long id) {
        List<Product> products = categoryRepository.findProductsByCategoryId(id);

        return products.stream().map(
                        product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}
