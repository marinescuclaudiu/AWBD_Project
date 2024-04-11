package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.dto.UserProfileDTO;
import com.awbd.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                categoryService.findById(id)
        );
    }


    @PostMapping
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO newCategory) {
        return ResponseEntity.ok().body(
                categoryService.save(newCategory)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO categoryDTO,
                                            @PathVariable Long id) {
        return ResponseEntity.ok().body(
                categoryService.update(id, categoryDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(categoryService.update(id, categoryDTO));
    }
}
