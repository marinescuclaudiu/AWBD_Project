package com.awbd.ecommerce.mapper;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Product;
import org.mapstruct.Mapper;
@Mapper
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);

    Product toProduct(ProductDTO productDTO);
}
