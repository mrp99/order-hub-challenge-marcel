package com.orderhub.api.repositories;

import com.orderhub.api.domain.Order;
import org.springframework.data.domain.Page;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull UUID> {
    List<Order> findByCustomerId(UUID customerId);
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items",
            countQuery = "SELECT count(o) FROM Order o")
    Page<@NonNull Order> findAllWithItems(Pageable pageable);
}
