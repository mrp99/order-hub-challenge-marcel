package com.orderhub.api.mapper;

import com.orderhub.api.domain.Inventory;
import com.orderhub.api.dtos.InventoryResponseDTO;

public class InventoryMapper {

    public InventoryResponseDTO toResponse(Inventory entity) {
        return new InventoryResponseDTO(
                entity.getId(),
                entity.getAvailableQuantity(),
                entity.getReservedQuantity()
        );
    }
}
