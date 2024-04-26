package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/categories/category-list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        model.addAttribute("category", categoryDTO);

        // extract all the products corresponding to this category
        List<ProductDTO> productsDTO = categoryService.findProductsByCategoryId(id);
        model.addAttribute("products", productsDTO);

        return "/categories/category-details";
    }

    @RequestMapping("/form")
    public String save(Model model) {
        model.addAttribute("category", new Category());
        return "categories/category-form";
    }

    @RequestMapping("/edit/{id}")
    public String update(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        model.addAttribute("category", categoryDTO);

        return "/categories/category-form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDTO);
            return "categories/category-form";
        }

        categoryService.save(categoryDTO);
        return "redirect:categories" ;
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
