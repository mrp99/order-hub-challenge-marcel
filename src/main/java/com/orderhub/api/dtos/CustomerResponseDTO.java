package com.orderhub.api.dtos;

import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String document,
        String name,
        String email
) {}
