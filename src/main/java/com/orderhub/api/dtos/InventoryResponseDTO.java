package com.orderhub.api.dtos;

import java.util.UUID;

public record InventoryResponseDTO(
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity
) {}
