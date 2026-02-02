-- Adiciona a coluna para a chave de idempotência
ALTER TABLE orders ADD COLUMN idempotency_key VARCHAR(255);

-- Garante que não existam chaves duplicadas (Segurança para o diferencial)
ALTER TABLE orders ADD CONSTRAINT uc_orders_idempotency_key UNIQUE (idempotency_key);