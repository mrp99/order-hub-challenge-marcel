-- 1. Inserir um Cliente de Teste
INSERT INTO customers (id, document, name, email)
VALUES ('550e8400-e29b-41d4-a716-446655440000', '12345678901', 'João Silva', 'joao@email.com')
ON CONFLICT (id) DO NOTHING;

-- 2. Inserir alguns Produtos
INSERT INTO products (id, sku, name, price, active)
VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'PROD-001', 'Smartphone Android', 1500.00, TRUE),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'PROD-002', 'Fone de Ouvido Bluetooth', 250.00, TRUE),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'PROD-003', 'Carregador Rápido', 80.00, TRUE)
ON CONFLICT (id) DO NOTHING;

-- 3. Inicializar o Estoque (Inventory)
-- Agora batendo 100% com sua tabela: 4 colunas e 4 valores.
INSERT INTO inventory (product_id, available_quantity, reserved_quantity, version)
VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 10, 0, 0),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 50, 0, 0),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 0, 0, 0)
ON CONFLICT (product_id) DO NOTHING;