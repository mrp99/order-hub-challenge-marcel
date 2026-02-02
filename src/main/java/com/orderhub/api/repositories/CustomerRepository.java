package com.orderhub.api.repositories;

import com.orderhub.api.domain.Customer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<@NonNull Customer,@NonNull UUID> {
    // Para buscar e usar o objeto
    Optional<Customer> findByDocument(String document);
    Optional<Customer> findByEmail(String email);

    // Para validações rápidas
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);
}
