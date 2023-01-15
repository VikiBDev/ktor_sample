ALTER TABLE points_balance
    ADD last_seen_balance int         NOT NULL DEFAULT 0,
    ADD seen_at           timestamptz NOT NULL DEFAULT current_timestamp,
    ADD changed_at        timestamptz NOT NULL DEFAULT current_timestamp;
