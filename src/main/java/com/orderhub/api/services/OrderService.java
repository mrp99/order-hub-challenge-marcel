package com.orderhub.api.services;

import com.orderhub.api.domain.Order;
import com.orderhub.api.domain.OrderItem;
import com.orderhub.api.domain.Product;
import com.orderhub.api.dtos.OrderItemRequestDTO;
import com.orderhub.api.dtos.OrderRequestDTO;
import com.orderhub.api.dtos.OrderResponseDTO;
import com.orderhub.api.enums.OrderStatus;
import com.orderhub.api.exceptions.BusinessException;
import com.orderhub.api.mapper.OrderMapper;
import com.orderhub.api.repositories.CustomerRepository;
import com.orderhub.api.repositories.OrderRepository;
import com.orderhub.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryService inventoryService;
    private final OrderMapper orderMapper;

    private static final String DUPLICATE_ORDER_MESSAGE ="Pedido já processado.";
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Cliente informado não encontrado.";
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Produto não encontrado.";
    private static final String ORDER_NOT_FOUND_MESSAGE = "Pedido não encontrado: ";
    private static final String ORDER_CANNOT_BE_CONFIRMED = "Apenas pedidos em estado CREATED podem ser confirmados.";
    private static final String ORDER_ALREADY_CANCELLED = "Este pedido já está cancelado.";


    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            InventoryService inventoryService,
            OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // 1. Idempotência
        orderRepository.findByIdempotencyKey(dto.idempotencyKey())
                .ifPresent(o -> {
                    throw new BusinessException(DUPLICATE_ORDER_MESSAGE);
                });

        // 2. Validação do Cliente
       if (!customerRepository.existsById(dto.customerId())) {
           throw new BusinessException(CUSTOMER_NOT_FOUND_MESSAGE);
       }

        Order order = new Order();
        order.setCustomerId(dto.customerId());
        order.setIdempotencyKey(dto.idempotencyKey());
        order.setStatus(OrderStatus.CREATED);

        // 3. Processar Itens e Reservar Estoque
        for (OrderItemRequestDTO itemDto : dto.items()) {

            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_MESSAGE));

            // Reserva o estoque usando o Lock Pessimista definido no InventoryService
            inventoryService.reserveStock(product.getId(), itemDto.quantity());

            // Adiciona o item ao pedido (calculando subtotal internamente)
            order.addItem(product.getId(), itemDto.quantity(), product.getPrice());
        }
        // Salva a ordem (o cascade=ALL salvará os itens automaticamente)
        return orderMapper.toResponse(orderRepository.save(order));
    }

    /**
     * Busca individual para o endpoint GET /api/orders/{id}
     */
    @Transactional
    public OrderResponseDTO findById(UUID id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND_MESSAGE + id));
    }

    /**
     * Listagem paginada usando o JOIN FETCH do seu Repository para evitar N+1
     */
    @Transactional
    public Page<@NonNull OrderResponseDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional
    public void confirmOrder(UUID orderId) {
        // 1. Busca o pedido ou lança erro
        Order order = getOrderOrThrow(orderId);

        // 2. Valida se o status permite confirmação
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BusinessException(ORDER_CANNOT_BE_CONFIRMED);
        }

        // 3. Atualiza o status
        order.setStatus(OrderStatus.CONFIRMED);

        // 4. Efetiva a baixa no estoque para cada item
        for (OrderItem item : order.getItems()) {
            inventoryService.confirmReservation(item.getProductId(), item.getQuantity());
        }
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        // 1. Busca o pedido
        Order order = getOrderOrThrow(orderId);

        // 2. Atualiza o status
        order.setStatus(OrderStatus.CANCELLED);

        // 4. Devolve o estoque reservado para o disponível
        for (OrderItem item : order.getItems()) {
            inventoryService.releaseReservation(
                    item.getProductId(),
                    item.getQuantity()
            );
        }
        orderRepository.save(order);
    }

    private Order getOrderOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND_MESSAGE + id));
    }
}
