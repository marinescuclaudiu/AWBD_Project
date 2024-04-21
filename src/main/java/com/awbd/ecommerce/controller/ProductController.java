package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<ProductDTO> products = productService.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);

        // count the number of reviews of this product
        int numberOfReviews = productDTO.getReviews().size();
        model.addAttribute("numberOfReviews", numberOfReviews);

        double averageRating = productService.getAverageRatingByProductId(id);
        model.addAttribute("averageRating", averageRating);

        return "product-details";
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    @RequestMapping("/form")
    public String addProduct(Model model) {
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
    public String saveOrUpdate(@ModelAttribute ProductDTO product,
                               @RequestParam("imagefile") MultipartFile file) {
        if (file.isEmpty())
            productService.save(product);
        else
            productService.savePhotoFile(product, file);


        return "redirect:/products" ;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handlerNotFoundException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception",exception);
        modelAndView.setViewName("notFoundException");
        return modelAndView;
    }
}
