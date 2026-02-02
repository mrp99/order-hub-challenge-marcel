package com.orderhub.api.enums;

public enum AppMessages {
    PRODUCT_NOT_FOUND("Produto não encontrado com o ID: %s"),
    CUSTOMER_NOT_FOUND("Cliente não encontrado com o ID: %s"),
    INSUFFICIENT_STOCK("Estoque insuficiente para o produto: %s"),
    INVENTORY_NOT_FOUND("Registro de estoque não encontrado para o produto: %s"),
    ORDER_NOT_FOUND("Pedido não encontrado com o ID: %s"),
    INVALID_ORDER_STATUS("Não é possível confirmar um pedido com status:"),
    CANCEL_INVALID_STATUS("Apenas pedidos no status CREATED podem ser cancelados."),
    ORDER_ALREADY_PROCESSED("Pedido já processado com esta chave");

    private final String message;

    AppMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    // Método útil para formatar mensagens com IDs ou nomes
    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
