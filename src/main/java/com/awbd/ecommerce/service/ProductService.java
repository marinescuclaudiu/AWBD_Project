package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO save(ProductDTO product);
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
    void deleteById(Long id);
    ProductDTO update(Long id, ProductDTO productDTO);
    List<ReviewDTO> getReviewsOfProductByProductId(Long id);

    double getAverageRatingByProductId(Long id);

    void savePhotoFile(ProductDTO product, MultipartFile file);

}
