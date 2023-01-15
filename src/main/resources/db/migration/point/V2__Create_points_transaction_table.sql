CREATE TYPE points_transaction_type AS ENUM ('STORE_REWARD_PURCHASE', 'INVOICE_PAYMENT');

CREATE TABLE points_transaction
(
    id                uuid PRIMARY KEY,
    points_balance_id BIGINT                  NOT NULL REFERENCES points_balance (id) ON DELETE CASCADE,
    amount            BIGINT                  NOT NULL,
    type              points_transaction_type NOT NULL,
    created_at        timestamptz             NOT NULL DEFAULT current_timestamp,
    updated_at        timestamptz             NOT NULL DEFAULT current_timestamp
);

CREATE INDEX ON points_transaction (points_balance_id);

CREATE TRIGGER points_transaction_updated_at
    BEFORE UPDATE
    ON points_transaction
    FOR EACH ROW
EXECUTE PROCEDURE core.updated_at();
