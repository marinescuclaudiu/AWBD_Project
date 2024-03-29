package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = new LinkedList<>();
        productRepository.findAll(Sort.by("name")
        ).iterator().forEachRemaining(products::add);

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}
