CREATE TABLE reward
(
    id                 uuid PRIMARY KEY,
    user_id            uuid REFERENCES "user".user (id),
    reward_metadata_id bigint      NOT NULL,
    created_at         timestamptz NOT NULL DEFAULT current_timestamp,
    updated_at         timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TRIGGER reward_updated_at
    BEFORE UPDATE
    ON reward
    FOR EACH ROW
EXECUTE PROCEDURE core.updated_at();

