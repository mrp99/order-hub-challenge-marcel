package com.orderhub.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "O ID do cliente é obrigatório.")
        UUID customerId,

        @NotBlank(message = "A chave de idempotência é obrigatória para garantir o processamento único.")
        String idempotencyKey,
        String items
) {}
