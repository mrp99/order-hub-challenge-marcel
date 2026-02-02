package com.orderhub.api.services;

import com.orderhub.api.domain.Inventory;
import com.orderhub.api.domain.Product;
import com.orderhub.api.dtos.InventoryRequestDTO;
import com.orderhub.api.dtos.InventoryResponseDTO;
import com.orderhub.api.enums.AppMessages;
import com.orderhub.api.exceptions.BusinessException;
import com.orderhub.api.repositories.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private static final String INVENTORY_NOT_FOUND_MESSAGE = "Produto não encontrado no inventário.";
    private static final String INSUFFICIENT_STOCK_MESSAGE = "Estoque insuficiente para o produto.";
    private static final String INSUFFICIENT_RESERVATION_MESSAGE = "Erro de integridade: Reserva insuficiente.";
    private static final String INSUFFICIENT_RELEASE_MESSAGE = "Erro: Quantidade reservada insuficiente para devolução.";

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    /**
     * Busca o inventário por ID do produto.
     */
    public Inventory findByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(
                        AppMessages.INVENTORY_NOT_FOUND.format(productId))
                );
    }
    /**
     * Cria o registro inicial de estoque.
     * Chamado pelo ProductService no momento da criação do produto.
     */
    @Transactional
    public void createInitialInventory(Product product) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setAvailableQuantity(0);
        inventory.setReservedQuantity(0);
        inventoryRepository.save(inventory);
    }
    /**
     * Atualiza a quantidade disponível (PUT /api/inventory/{productId}).
     * Implementado conforme o README.
     */
    @Transactional
    public void updateAvailableQuantity(UUID productId, InventoryRequestDTO dto) {
        Inventory inventory = findByProductId(productId);
        inventory.setAvailableQuantity(dto.availableQuantity());
    }
    /**
     * Realiza a reserva de estoque (Bloqueio Pessimista).
     * Chamado durante a criação do Pedido.
     */
    @Transactional
    public void reserveStock(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(INVENTORY_NOT_FOUND_MESSAGE));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new BusinessException(INSUFFICIENT_STOCK_MESSAGE + productId);
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
    }
    /**
     * Confirma a venda: Apenas remove do reservado.
     */
    @Transactional
    public void confirmReservation(UUID productId, Integer quantity) {
        Inventory inventory = findByProductId(productId);

        if (inventory.getReservedQuantity() < quantity) {
            throw new BusinessException(INSUFFICIENT_RESERVATION_MESSAGE + productId);
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
    }
    /**
     * Cancela a reserva: Devolve do reservado para o disponível.
     */
    @Transactional
    public void releaseReservation(UUID productId, Integer quantity) {
        Inventory inventory = findByProductId(productId);
        if (inventory.getReservedQuantity() < quantity) {
            throw new BusinessException(INSUFFICIENT_RELEASE_MESSAGE);
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
    }
    /**
     * Retorna o DTO de resposta para o Controller.
     */
    public InventoryResponseDTO getDetailsByProductId(UUID productId) {
        Inventory inventory = findByProductId(productId);
        return new InventoryResponseDTO(
                inventory.getProduct().getId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity()
        );
    }
}
