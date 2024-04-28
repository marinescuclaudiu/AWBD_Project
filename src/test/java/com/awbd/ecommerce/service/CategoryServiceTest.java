package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.CategoryDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Category;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    ModelMapper modelMapper;

    @Test
    public void saveCategory_Success() {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");

        Category categoryToSave = new Category();
        categoryToSave.setName("Test Category");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Test Category");

        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);
        when(modelMapper.map(savedCategory, CategoryDTO.class)).thenReturn(categoryDTO);

        // Act
        CategoryDTO savedDTO = categoryService.save(categoryDTO);

        // Assert
        assertNotNull(savedDTO);
        assertEquals(categoryDTO.getName(), savedDTO.getName());

        verify(categoryRepository, times(1)).save(categoryToSave);
        verify(modelMapper, times(1)).map(categoryDTO, Category.class);
        verify(modelMapper, times(1)).map(savedCategory, CategoryDTO.class);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void findAll_Success() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        List<Category> categoryList = Arrays.asList(category1, category2);

        CategoryDTO categoryDto1 = new CategoryDTO();
        categoryDto1.setId(1L);
        categoryDto1.setName("Category 1");

        CategoryDTO categoryDto2 = new CategoryDTO();
        categoryDto2.setId(2L);
        categoryDto2.setName("Category 2");

        List<CategoryDTO> expectedDtoList = Arrays.asList(categoryDto1, categoryDto2);

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(modelMapper.map(category1, CategoryDTO.class)).thenReturn(categoryDto1);
        when(modelMapper.map(category2, CategoryDTO.class)).thenReturn(categoryDto2);

        // Act
        List<CategoryDTO> result = categoryService.findAll();

        // Assert
        assertEquals(expectedDtoList.size(), result.size());
        for (int i = 0; i < expectedDtoList.size(); i++) {
            assertEquals(expectedDtoList.get(i), result.get(i));
        }

        verify(categoryRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(category1, CategoryDTO.class);
        verify(modelMapper, times(1)).map(category2, CategoryDTO.class);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void findById_Success() {
        // Arrange
        long categoryId = 1L;
        String categoryName = "Test Category";
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        Optional<Category> optionalCategory = Optional.of(category);

        CategoryDTO expectedDto = new CategoryDTO();
        expectedDto.setId(categoryId);
        expectedDto.setName(categoryName);

        when(categoryRepository.findById(categoryId)).thenReturn(optionalCategory);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(expectedDto);

        // Act
        CategoryDTO result = categoryService.findById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(modelMapper, times(1)).map(category, CategoryDTO.class);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void findById_CategoryNotFound() {
        // Arrange
        long categoryId = 1L;
        Optional<Category> optionalCategory = Optional.empty();

        when(categoryRepository.findById(categoryId)).thenReturn(optionalCategory);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void deleteById_Success() {
        // Arrange
        long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // Act
        categoryService.deleteById(categoryId);

        // Assert
        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void deleteById_CategoryNotFound() {
        // Arrange
        long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteById(categoryId));

        verify(categoryRepository, times(1)).existsById(categoryId);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void update_Success() {
        // Arrange
        long categoryId = 1L;
        String categoryName = "Test Category";
        CategoryDTO updatedDto = new CategoryDTO();
        updatedDto.setName("Updated Test Category");
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        Optional<Category> optionalCategory = Optional.of(category);

        when(categoryRepository.findById(categoryId)).thenReturn(optionalCategory);
        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(updatedDto);

        // Act
        CategoryDTO result = categoryService.update(categoryId, updatedDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDto, result);
        assertEquals(updatedDto.getName(), result.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
        verify(modelMapper, times(1)).map(category, CategoryDTO.class);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void update_CategoryNotFound() {
        // Arrange
        long categoryId = 1L;
        CategoryDTO updatedDto = new CategoryDTO();
        updatedDto.setName("Updated Test Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(categoryId, updatedDto));

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

    @Test
    public void findProductsByCategoryId_Success() {
        // Arrange
        long categoryId = 1L;
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

        when(categoryRepository.findProductsByCategoryId(categoryId)).thenReturn(productList);
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(productDto1);
        when(modelMapper.map(product2, ProductDTO.class)).thenReturn(productDto2);

        // Act
        List<ProductDTO> result = categoryService.findProductsByCategoryId(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDtoList.size(), result.size());
        for (int i = 0; i < expectedDtoList.size(); i++) {
            assertEquals(expectedDtoList.get(i), result.get(i));
        }

        verify(categoryRepository, times(1)).findProductsByCategoryId(categoryId);
        verify(modelMapper, times(1)).map(product1, ProductDTO.class);
        verify(modelMapper, times(1)).map(product2, ProductDTO.class);
        verifyNoMoreInteractions(categoryRepository, modelMapper);
    }

}
