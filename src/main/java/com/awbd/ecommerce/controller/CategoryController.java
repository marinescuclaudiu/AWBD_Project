package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok().body(categoryService.save(categoryDTO));
    }

    @GetMapping("/public/category/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @PatchMapping("/admin/category/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(categoryService.update(id, categoryDTO));
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
