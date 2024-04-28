package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
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

        return "category-list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        try {
            CategoryDTO categoryDTO = categoryService.findById(Long.valueOf(id));
            model.addAttribute("category", categoryDTO);

            // extract all the products corresponding to this category
            List<ProductDTO> productsDTO = categoryService.findProductsByCategoryId(Long.valueOf(id));
            model.addAttribute("products", productsDTO);

            return "category-details";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @RequestMapping("/form")
    public String save(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    @RequestMapping("/edit/{id}")
    public String update(@PathVariable String id, Model model) {
        try {
            CategoryDTO categoryDTO = categoryService.findById(Long.valueOf(id));
            model.addAttribute("category", categoryDTO);
            return "category-form";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDTO);
            return "category-form";
        }

        categoryService.save(categoryDTO);
        return "redirect:/categories" ;
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable String id, Model model) {
        try {
            categoryService.deleteById(Long.valueOf(id));
            return "redirect:/categories";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }
}
