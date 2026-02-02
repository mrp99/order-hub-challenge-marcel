package com.orderhub.api.domain;

import com.orderhub.api.exceptions.BusinessException;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private UUID productId; // Este campo recebe o ID do produto via @MapsId

    @OneToOne
    @MapsId //"Use o ID do produto como o ID desta tabela"
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Version // Lock Otimista
    private Long version;

    //JPA
    public Inventory() {}

    // Construtor útil para inicializar estoque
    public Inventory(Product product, Integer availableQuantity) {
        this.product = product;
        this.productId = product.getId();
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
    }

    //Getters and Setters
    public UUID getId() { return productId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public Long getVerision() { return version; }

    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("A quantidade deve ser maior que zero.");
        }
        if (this.availableQuantity < quantity) {
            throw new BusinessException("Estoque insuficiente para o produto: " + this.productId);
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(productId, inventory.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }

    @Override
    public String toString() {
        return String.format(
                "Inventory [Product ID: %s | Avail: %d | Resv: %d | v.%d]",
                productId, availableQuantity, reservedQuantity, version
        );
    }
}
