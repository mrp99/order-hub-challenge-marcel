package com.orderhub.api.mapper;

import com.orderhub.api.domain.Order;
import com.orderhub.api.domain.OrderItem;
import com.orderhub.api.dtos.OrderRequestDTO;
import com.orderhub.api.dtos.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDTO dto){
        Order order = new Order();
        order.setCustomerId(dto.customerId());
        order.setIdempotencyKey(dto.idempotencyKey());
        if (dto.items() != null) {
            dto.items().forEach(item ->
                    order.addItem(
                            item.productId(),
                            item.quantity(),
                            BigDecimal.ZERO
                    ));
        }
        return order;
    }

    public OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
            order.getId(), //id
            order.getCustomerId(), //customerId
            order.getStatus(), //status
            order.getTotalAmount(), //totalAmount
            order.getCreatedAt(),  //createdAt
                order.getItems() == null ? 0 : order.getItems().stream()
                        .mapToInt(OrderItem::getQuantity)
                        .sum(),
            order.getIdempotencyKey()
        );
    }
}
