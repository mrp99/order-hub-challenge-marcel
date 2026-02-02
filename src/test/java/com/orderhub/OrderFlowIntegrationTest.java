package com.orderhub;

import com.orderhub.api.dtos.*;
import com.orderhub.api.repositories.InventoryRepository;
import com.orderhub.api.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class OrderFlowIntegrationTest {

    @Autowired private OrderService orderService;
    @Autowired private InventoryRepository inventoryRepository;

    // Utilize os UUIDs que você inseriu na sua migration V2__insert_test_data.sql
    private final UUID CLIENTE_ID =
            UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private final UUID PRODUTO_ID =
            UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22");

    @Test
    @DisplayName("Deve criar pedido e reservar estoque com sucesso")
    void deveProcessarPedidoComSucesso() {
        OrderRequestDTO request = new OrderRequestDTO(
                CLIENTE_ID,
                List.of(new OrderItemRequestDTO( // REMOVA o .toString() daqui
                        PRODUTO_ID,
                        2,
                        BigDecimal.valueOf(100)
                )).toString(), // Passa a List diretamente
                "chave-sucesso-001"
        );

        OrderResponseDTO response = orderService.createOrder(request);
        Assertions.assertNotNull(response.id());

        var estoque = inventoryRepository.findByProductId(PRODUTO_ID).get();
        Assertions.assertEquals(2, estoque.getReservedQuantity());
    }
}
