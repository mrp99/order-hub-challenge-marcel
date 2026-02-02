package com.orderhub.api.services;

import com.orderhub.api.domain.Product;
import com.orderhub.api.dtos.ProductRequestDTO;
import com.orderhub.api.dtos.ProductResponseDTO;
import com.orderhub.api.exceptions.BusinessException;
import com.orderhub.api.mapper.ProductMapper;
import com.orderhub.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryService inventoryService;

    private static final String SKU_ERROR_MESSAGE = "SKU already exists";
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            InventoryService inventoryService
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        if (productRepository.existsBySku(dto.sku())){
            throw new BusinessException(SKU_ERROR_MESSAGE);
        }
        // 1. Converte DTO para Entidade
        Product product = productMapper.toEntity(dto);
        // 2. Salva o produto
        Product savedProduct = productRepository.save(product);
        // 3. Cria o registro de inventário inicial
        inventoryService.createInitialInventory(savedProduct);
        // 4. Retorna o ResponseDTO via Mapper
        return productMapper.toResponse(savedProduct);
    }

    // Método de busca por ID com tratamento de erro
    public ProductResponseDTO findById(java.util.UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public List<ProductResponseDTO> findAll(Boolean active) {
        List<Product> products;
        if (active != null) {
            products = productRepository.findByActive(active);
        } else {
            products = productRepository.findAll();
        }

        return  products.stream()
                .map(productMapper::toResponse)
                .toList();
    }
}
