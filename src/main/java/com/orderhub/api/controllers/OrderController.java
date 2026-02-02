package com.orderhub.api.controllers;

import com.orderhub.api.dtos.OrderRequestDTO;
import com.orderhub.api.dtos.OrderResponseDTO;
import com.orderhub.api.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /**
     * Cria um novo pedido com reserva de estoque e idempotência.
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody @Valid OrderRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(dto));
    }

    /**
     * Busca os detalhes de um pedido específico por ID.
     * Requisito fundamental para o rastreio do pedido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById (@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    /**
     * Lista pedidos de forma paginada usando o repositório otimizado (Join Fetch).
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> listAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    /**
     * Confirma o pedido e efetiva a baixa do estoque reservado.
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable UUID id) {
        orderService.confirmOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela o pedido e libera o estoque reservado.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
