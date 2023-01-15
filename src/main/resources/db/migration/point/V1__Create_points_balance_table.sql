CREATE TABLE points_balance
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    uuid        NOT NULL REFERENCES "user"."user" (id) ON DELETE CASCADE,
    amount     bigint      NOT NULL DEFAULT 0,
    created_at timestamptz NOT NULL DEFAULT current_timestamp,
    updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE INDEX ON points_balance (user_id);

CREATE TRIGGER points_balance_updated_at
    BEFORE UPDATE
    ON points_balance
    FOR EACH ROW
EXECUTE PROCEDURE core.updated_at();
