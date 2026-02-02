package com.orderhub.api.controllers;

import com.orderhub.api.dtos.InventoryRequestDTO;
import com.orderhub.api.dtos.InventoryResponseDTO;
import com.orderhub.api.dtos.InventoryRestockDTO;
import com.orderhub.api.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Atualiza a quantidade disponível de um produto.
     * Requisito: PUT /api/inventory/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateAvailableQuantity(
            @PathVariable UUID productId,
            @RequestBody @Valid InventoryRequestDTO dto
    ) {
        inventoryService.updateAvailableQuantity(productId, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retorna o status detalhado do estoque de um produto (Disponível vs Reservado).
     */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getDetailsByProductId(productId));
    }
}
