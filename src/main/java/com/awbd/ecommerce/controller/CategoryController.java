package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<CategoryDTO> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        isLoggedIn(model);

        return "/categories/category-list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        model.addAttribute("category", categoryDTO);

        // extract all the products corresponding to this category
        List<ProductDTO> productsDTO = categoryService.findProductsByCategoryId(id);
        model.addAttribute("products", productsDTO);

        isLoggedIn(model);

        return "/categories/category-details";
    }

    @RequestMapping("/form")
    public String save(Model model) {
        model.addAttribute("category", new Category());

        isLoggedIn(model);

        return "categories/category-form";
    }

    @RequestMapping("/edit/{id}")
    public String update(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(id);

        model.addAttribute("category", categoryDTO);
        isLoggedIn(model);

        return "/categories/category-form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                               BindingResult bindingResult,
                               Model model) {
        isLoggedIn(model);

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

    private void isLoggedIn(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            UserDTO userDTO = userService.findByUsername(auth.getName());
            model.addAttribute("loggedUserId", userDTO.getId());
        } else {
            model.addAttribute("loggedUserId", null);
        }
    }
}
