package com.orderhub.api.controllers;

import com.orderhub.api.dtos.ProductRequestDTO;
import com.orderhub.api.dtos.ProductResponseDTO;
import com.orderhub.api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Requisito: Criação de produto com validação.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO newProduct = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    /**
     * Requisito Mínimo: GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        ProductResponseDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Requisito: GET /api/products?active=true|false
     * Implementado com suporte a filtro opcional.
     */
    @GetMapping
    public ResponseEntity< List<ProductResponseDTO>> listAll(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(productService.findAll(active));
    }
}
