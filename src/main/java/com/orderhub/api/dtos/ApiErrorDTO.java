package com.orderhub.api.dtos;

public record ApiErrorDTO(
        String error,
        String message
) { }
