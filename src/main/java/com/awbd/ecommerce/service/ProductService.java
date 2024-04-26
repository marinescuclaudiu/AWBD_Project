package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO save(ProductDTO product);
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
    void deleteById(Long id);
    ProductDTO update(Long id, ProductDTO productDTO);
    List<ReviewDTO> getReviewsOfProductByProductId(Long id);

//    List<ProductDTO> findByCategoryAndSortByPrice(String categoryName);

    double getAverageRatingByProductId(Long id);

    void savePhotoFile(ProductDTO product, MultipartFile file);
    Page<ProductDTO> findPaginated(Pageable pageable, String categoryName);
}
