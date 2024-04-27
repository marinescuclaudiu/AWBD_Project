package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.service.CategoryService;
import com.awbd.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceControllerTest {
    @Mock
    Model model;
    @Mock
    ProductService productService;

    @Mock
    CategoryService categoryService;
    @InjectMocks
    ProductController productController;
    @Mock
    ModelMapper modelMapper;

    @Test
    public void showById() {
        // arrange
        Long id = 1L;
        Product productTest = new Product();
        productTest.setId(id);

        ProductDTO productTestDTO = new ProductDTO();
        productTestDTO.setId(id);

        // act
        //when(modelMapper.map(productTest, ProductDTO.class)).thenReturn(productTestDTO);
        when(productService.findById(id)).thenReturn(productTestDTO);

        String viewName = productController.edit(id, model);

        // assert
//        assertEquals("products/product-form", viewName);
//        verify(productService, times(1)).findById(id);
//
//        ArgumentCaptor<ProductDTO> argumentCaptor = ArgumentCaptor.forClass(ProductDTO.class);
//        verify(model, times(1))
//                .addAttribute(eq(productTestDTO).toString(), argumentCaptor.capture() );
//
//        ProductDTO productArg = argumentCaptor.getValue();
//        assertEquals(productArg.getId(), productTestDTO.getId() );
    }
}
