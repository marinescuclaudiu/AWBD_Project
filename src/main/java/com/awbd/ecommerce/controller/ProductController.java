package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping
    public String productList(Model model) {
        List<ProductDTO> products = productService.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }
}
