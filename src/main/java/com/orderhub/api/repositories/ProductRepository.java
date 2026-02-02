package com.orderhub.api.repositories;

import com.orderhub.api.domain.Product;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<@NonNull Product, @NonNull UUID> {
    Optional<Product> findBySku(String sku);
    List<Product> findByActive(boolean active);
    boolean existsBySku(String sku);
}
