package com.orderhub.api.exceptions;

import com.orderhub.api.dtos.ApiErrorDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. CAPTURA ERROS DE VALIDAÇÃO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ApiErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiErrorDTO error = new ApiErrorDTO("VALIDATION_ERROR", details);
        return ResponseEntity.badRequest().body(error);
    }

    // 2. ERROS DE REGRA DE NEGÓCIO (Estoque insuficiente, Cliente não encontrado, etc)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<@NonNull ApiErrorDTO> handleBusinessException(BusinessException ex) {
        ApiErrorDTO error = new ApiErrorDTO("BUSINESS_RULE_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3. CONCORRÊNCIA (Lock Otimista) - MOSTRA NÍVEL SÊNIOR
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<@NonNull ApiErrorDTO> handleConcurrencyException() {
        ApiErrorDTO error = new ApiErrorDTO(
                "CONCURRENCY_ERROR",
                "O registro foi atualizado por outro processo. Tente novamente."
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 4. ERRO GENÉRICO
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiErrorDTO> handleGeneralException(Exception ex) {
        // Logar o erro ex.printStackTrace() ou logger.error
        ApiErrorDTO error = new ApiErrorDTO(
                "INTERNAL_SERVER_ERROR",
                "Ocorreu um erro inesperado."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}