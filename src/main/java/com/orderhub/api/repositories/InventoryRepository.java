package com.orderhub.api.repositories;

import com.orderhub.api.domain.Inventory;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<@NonNull Inventory, @NonNull UUID> {
    // Usa no Controller
    Optional<Inventory> findByProductId(UUID productId);

    // Lock pessimista para garantir exclusividade na reserva de estoque
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId")
    Optional<Inventory> findByIdForUpdate(UUID productId);
}
