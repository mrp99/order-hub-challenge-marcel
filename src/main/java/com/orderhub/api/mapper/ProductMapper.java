package com.orderhub.api.mapper;

import com.orderhub.api.domain.Product;
import com.orderhub.api.dtos.ProductRequestDTO;
import com.orderhub.api.dtos.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setSku(dto.sku());
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setActive(true);
        return product;
    }

    public ProductResponseDTO toResponse(Product entity) {
        return new ProductResponseDTO(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getPrice(),
                entity.isActive()
        );
    }
}
