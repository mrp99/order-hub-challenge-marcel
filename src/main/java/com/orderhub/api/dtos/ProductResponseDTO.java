package com.orderhub.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String sku,
        String name,
        BigDecimal price,
        boolean active
) {
}
