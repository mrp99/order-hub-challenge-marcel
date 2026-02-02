package com.orderhub.api.dtos;

import java.util.UUID;

public record InventoryRestockDTO(
        UUID productId,
        Integer quantity
) {}
