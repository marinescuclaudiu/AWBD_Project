package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.info("Saving product: {}", productDTO.getName());
        Product savedProduct = productRepository.save(modelMapper.map(productDTO, Product.class));

        log.info("Product saved: {}", productDTO.getName());
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> findAll() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();

        log.info("Found {} products", products.size());
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(Long id) {
        log.info("Fetching product by ID: {}", id);
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            log.error("Product with id {} not found!", id);
            throw new ResourceNotFoundException("Product with id" + id + " not found!");
        }

        log.info("Product found: {}", product.get().getName());
        return modelMapper.map(product.get(), ProductDTO.class);
    }

    //    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting product by ID: {}", id);

        if (!productRepository.existsById(id)) {
            log.error("Product with id {} not found", id);
            throw new ResourceNotFoundException("Product with id " + id + " not found!");
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully");
    }

    @Transactional
    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            log.error("Product with id {} not found", id);
            throw new ResourceNotFoundException("Product with id " + id + " not found!");
        }

        BeanUtils.copyProperties(productDTO, product.get(), BeanHelper.getNullPropertyNames(productDTO));

        productRepository.save(product.get());

        log.info("Product updated: {}", product.get().getName());
        return modelMapper.map(product.get(), ProductDTO.class);
    }

    @Override
    public List<ReviewDTO> getReviewsOfProductByProductId(Long id) {
        log.info("Fetching all reviews for product with id: {}", id);

        if (!productRepository.existsById(id)) {
            log.error("Product with id {} not found", id);
            throw new ResourceNotFoundException("Product with id " + id + " not found!");
        }

        List<Review> reviewsOfProduct = productRepository.getReviewsOfProductByProductId(id);

        log.info("Found {} reviews", reviewsOfProduct.size());
        return reviewsOfProduct.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageRatingByProductId(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found!"));

        // check if the product has reviews
        Product product = productRepository.findById(id).get();

        if (product.getReviews().isEmpty()) {
            return 0;
        }

        return (double) Math.round(productRepository.getAverageRatingByProductId(id) * 100) / 100;
    }

    @Override
    public void savePhotoFile(ProductDTO productDTO, MultipartFile file) {
        Product product = modelMapper.map(productDTO, Product.class);
        try {


            byte[] byteObjects = new byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            if (byteObjects.length > 0) {
                product.setPhoto(byteObjects);
            }

            productRepository.save(product);
        } catch (IOException e) {
        }
    }

    @Override
    public Page<ProductDTO> findPaginated(Pageable pageable, String categoryName) {
        if (categoryName == null) {
            System.out.println("-------------------");
            System.out.println("in null branch");

            List<ProductDTO> dtoList = productRepository
                    .findAll()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());

            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), dtoList.size());

            return new PageImpl<>(dtoList.subList(start, end), pageable, dtoList.size());
        }

        List<ProductDTO> productDTOFilteredList = productRepository
                .findByCategoryAndSortByPrice(categoryName)
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDTOFilteredList.size());

        return new PageImpl<>(
                productDTOFilteredList.subList(start, end),
                pageable,
                productDTOFilteredList.size()
        );
    }
}
