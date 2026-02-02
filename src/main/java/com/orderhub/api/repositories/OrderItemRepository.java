package com.orderhub.api.repositories;

import com.orderhub.api.domain.OrderItem;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<@NonNull OrderItem, @NonNull Long> {
    /*
    * Busca itens relacionados a um produto específico.
    * Pode ser usado para validar se um produto já foi vendido antes de excluí-lo.
    * */
    List<OrderItem> findByProductId(UUID productId);
}
