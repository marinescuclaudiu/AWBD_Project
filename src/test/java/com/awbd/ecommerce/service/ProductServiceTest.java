package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.ReviewDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.Review;
import com.awbd.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    ModelMapper modelMapper;

    @Test
    void save_Success() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");

        when(modelMapper.map(productDTO, Product.class)).thenReturn(new Product());
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct, ProductDTO.class)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.save(productDTO);

        // Assert
        assertEquals(productDTO.getName(), result.getName());
        verify(modelMapper).map(productDTO, Product.class);
        verify(productRepository).save(any());
        verify(modelMapper).map(savedProduct, ProductDTO.class);
    }

    @Test
    void findAll_Success() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);

        ProductDTO productDto1 = new ProductDTO();
        productDto1.setId(1L);
        productDto1.setName("Product 1");

        ProductDTO productDto2 = new ProductDTO();
        productDto2.setId(2L);
        productDto2.setName("Product 2");

        List<ProductDTO> expectedDtoList = Arrays.asList(productDto1, productDto2);

        when(productRepository.findAll()).thenReturn(productList);
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(productDto1);
        when(modelMapper.map(product2, ProductDTO.class)).thenReturn(productDto2);

        // Act
        List<ProductDTO> result = productService.findAll();

        // Assert
        assertEquals(expectedDtoList.size(), result.size());
        for (int i = 0; i < expectedDtoList.size(); i++) {
            assertEquals(expectedDtoList.get(i), result.get(i));
        }

        verify(productRepository).findAll();
        verify(modelMapper).map(product1, ProductDTO.class);
        verify(modelMapper).map(product2, ProductDTO.class);
    }

    @Test
    void findById_Success() {
        // Arrange
        long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        ProductDTO expectedDto = new ProductDTO();
        expectedDto.setId(productId);
        expectedDto.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(expectedDto);

        // Act
        ProductDTO result = productService.findById(productId);

        // Assert
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());

        verify(productRepository).findById(productId);
        verify(modelMapper).map(product, ProductDTO.class);
    }

    @Test
    void findById_ProductNotFound() {
        // Arrange
        long nonExistingProductId = 999L;
        when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingProductId));
        verify(productRepository).findById(nonExistingProductId);
    }

    @Test
    void deleteById_Success() {
        // Arrange
        long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteById(productId);

        // Assert
        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteById_ProductNotFound() {
        // Arrange
        long nonExistingProductId = 999L;

        when(productRepository.existsById(nonExistingProductId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(nonExistingProductId));

        // Assert
        verify(productRepository).existsById(nonExistingProductId);
    }

    @Test
    void update_Success() {
        // Arrange
        long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Original Product");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
        when(modelMapper.map(existingProduct, ProductDTO.class)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.update(productId, productDTO);

        // Assert
        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
        verify(modelMapper).map(existingProduct, ProductDTO.class);
    }

    @Test
    void update_ProductNotFound() {
        // Arrange
        long nonExistingProductId = 999L;
        ProductDTO productDTO = new ProductDTO();

        when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.update(nonExistingProductId, productDTO));

        // Assert
        verify(productRepository).findById(nonExistingProductId);
    }
    @Test
    void getReviewsOfProductByProductId_Success() {
        // Arrange
        long productId = 1L;
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(5);
        review1.setContent("Review 1");

        Review review2 = new Review();
        review2.setId(2L);
        review1.setRating(3);
        review2.setContent("Review 2");

        List<Review> reviewList = Arrays.asList(review1, review2);
        List<ReviewDTO> expectedDtoList = Arrays.asList(
                new ReviewDTO(1L, 5, "Review 1"),
                new ReviewDTO(2L, 3, "Review 2")
        );

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReviewsOfProductByProductId(productId)).thenReturn(reviewList);
        when(modelMapper.map(review1, ReviewDTO.class)).thenReturn(expectedDtoList.get(0));
        when(modelMapper.map(review2, ReviewDTO.class)).thenReturn(expectedDtoList.get(1));

        // Act
        List <ReviewDTO> result = productService.getReviewsOfProductByProductId(productId);

        // Assert
        assertEquals(expectedDtoList.size(), result.size());
        for (int i = 0; i < expectedDtoList.size(); i++) {
            assertEquals(expectedDtoList.get(i), result.get(i));
        }

        verify(productRepository).existsById(productId);
        verify(productRepository).getReviewsOfProductByProductId(productId);
        verify(modelMapper).map(review1, ReviewDTO.class);
        verify(modelMapper).map(review2, ReviewDTO.class);
    }
    @Test
    void getReviewsOfProductByProductId_ProductNotFound() {
        // Arrange
        long nonExistingProductId = 999L;
        when(productRepository.existsById(nonExistingProductId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getReviewsOfProductByProductId(nonExistingProductId));

        // Verify
        verify(productRepository).existsById(nonExistingProductId);
    }

    @Test
    void getAverageRatingByProductId_Success() {
        // Arrange
        long productId = 1L;
        double averageRating = 4.5;

        Product product = new Product();
        product.setId(productId);
        product.setReviews(Set.of(
                new Review(1L, 4, "Review 1"),
                new Review(2L, 5, "Review 2")
        ));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.getAverageRatingByProductId(productId)).thenReturn(averageRating);

        // Act
        double result = productService.getAverageRatingByProductId(productId);

        // Assert
        assertEquals(averageRating, result);
        verify(productRepository).findById(productId);
        verify(productRepository).getAverageRatingByProductId(productId);
    }


    @Test
    void getAverageRatingByProductId_ProductNotFound() {
        // Arrange
        long nonExistingProductId = 999L;
        when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getAverageRatingByProductId(nonExistingProductId));

        // Verify
        verify(productRepository).findById(nonExistingProductId);
    }

    @Test
    void savePhotoFile_Success(){
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());

        Product product = new Product();
        product.setId(productDTO.getId());

        when(modelMapper.map(productDTO, Product.class)).thenReturn(product);

        // Act
        productService.savePhotoFile(productDTO, file);

        // Assert
        verify(productRepository).save(product);
        assertNotNull(product.getPhoto());
        assertEquals("test image", new String(product.getPhoto()));
    }

    @Test
    void findPaginated_WhenCategoryNameIsNull_Success() {
        // Arrange
        int pageSize = 5;
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Product> products = createProductList(10); // Create a list of 10 products
        when(productRepository.findAll()).thenReturn(products);

        // Act
        Page<ProductDTO> result = productService.findPaginated(pageable, null);

        // Assert
        assertEquals(2, result.getTotalPages()); // We have 10 products and a page size of 5, so we should have 2 pages
        assertEquals(5, result.getContent().size()); // We expect 5 products in the first page
    }

    @Test
    void findPaginated_WhenCategoryNameIsNotNull_Success() {
        // Arrange
        int pageSize = 5;
        String categoryName = "Test Category";
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Product> products = createProductList(10); // Create a list of 10 products
        when(productRepository.findByCategoryAndSortByPrice(categoryName)).thenReturn(products);

        // Act
        Page<ProductDTO> result = productService.findPaginated(pageable, categoryName);

        // Assert
        assertEquals(2, result.getTotalPages()); // We have 10 products and a page size of 5, so we should have 2 pages
        assertEquals(5, result.getContent().size()); // We expect 5 products in the first page
        // Add more assertions if necessary
    }

    // Helper method to create a list of products for testing
    private List<Product> createProductList(int numberOfProducts) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= numberOfProducts; i++) {
            Product product = new Product();
            // Set necessary properties for the product
            products.add(product);
        }
        return products;
    }


}
