package com.orderhub.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryRequestDTO(
        @NotNull(message = "A quantidade disponível é obrigatória.")
        @PositiveOrZero(message = "A quantidade disponível não pode ser negativa.")
        Integer availableQuantity
) {}
