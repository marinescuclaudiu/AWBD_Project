package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @RequestMapping
    public String getProductPage(Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size,
                                 @RequestParam(required = false, value = "category") String category) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ProductDTO> productPage = productService.findPaginated(
                PageRequest.of(currentPage - 1, pageSize), category
        );

        model.addAttribute("productPage", productPage);
        model.addAttribute("category", category);

        return "product-list";
    }

    @RequestMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        try {
            ProductDTO productDTO = productService.findById(Long.valueOf(id));
            model.addAttribute("product", productDTO);

            // count the number of reviews of this product
            int numberOfReviews = productDTO.getReviews().size();
            model.addAttribute("numberOfReviews", numberOfReviews);

            double averageRating = productService.getAverageRatingByProductId(Long.valueOf(id));
            model.addAttribute("averageRating", averageRating);

            return "product-details";
        } catch (ResourceAccessException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, Model model) {
        try {
            productService.deleteById(id);
            return "redirect:/products";
        } catch (ResourceAccessException exception) {
            model.addAttribute("exception", exception);
            return("notFoundException");
        }
    }

    @RequestMapping("/form")
    public String showProductForm(Model model) {
        Product product = new Product();
        product.setReviews(null);

        model.addAttribute("product", product);

        // extract all categories
        List<CategoryDTO> allCategories = categoryService.findAll();
        model.addAttribute("allCategories", allCategories);

        return "product-form";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);

        // extract all categories
        List<CategoryDTO> allCategories = categoryService.findAll();
        model.addAttribute("allCategories", allCategories);

        return "product-form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("product") ProductDTO product,
                               BindingResult bindingResult,
                               Model model,
                               @RequestParam("imagefile") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);

            List<CategoryDTO> allCategories = categoryService.findAll();
            model.addAttribute("allCategories", allCategories);

            return "product-form";
        }

        if (file.isEmpty())
            productService.save(product);
        else
            productService.savePhotoFile(product, file);


        return "redirect:/products";
    }

    @GetMapping("/getimage/{id}")
    public void downloadImage(@PathVariable String id, HttpServletResponse response) {
        ProductDTO productDTO = productService.findById(Long.valueOf(id));

        if (productDTO.getPhoto() != null) {
            byte[] byteArray = new byte[productDTO.getPhoto().length];
            int i = 0;
            for (Byte wrappedByte : productDTO.getPhoto()) {
                byteArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);

            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
