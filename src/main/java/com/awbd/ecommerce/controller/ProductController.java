package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

//    @GetMapping
//    public String findAll(Model model) {
//        List<ProductDTO> products = productService.findAll();
//        model.addAttribute("products", products);
//        return "/products/product-list";
//    }

    @RequestMapping // /movies?page=1&size=10
    public String getMoviePage(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ProductDTO> moviePage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("productPage",moviePage);

        return "/products/product-list";
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

        return "products/product-details";
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

        return "products/product-form";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);

        // extract all categories
        List<CategoryDTO> allCategories = categoryService.findAll();
        model.addAttribute("allCategories", allCategories);

        return "products/product-form";
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

            return "products/product-form";
        }

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
