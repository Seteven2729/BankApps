CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    balance NUMERIC(15, 2) DEFAULT 0.00
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    type VARCHAR(10) NOT NULL,          -- 'DEPOSIT' or 'WITHDRAW'
    amount NUMERIC(15, 2) NOT NULL,
    balance_after NUMERIC(15,2) NOT NULL,
    request_id VARCHAR(100) NOT NULL,   -- For idempotency
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uniq_request_per_type UNIQUE (request_id, type)
);


CREATE INDEX idx_transactions_account_id ON transactions(account_id);
