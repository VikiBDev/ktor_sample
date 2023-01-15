create table user_credentials
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         uuid               NOT NULL REFERENCES "user"."user" (id) ON DELETE CASCADE,
    username        varchar(64) UNIQUE NOT NULL,
    password_hashed varchar(128)       NOT NULL,
    created_at      timestamptz        NOT NULL DEFAULT current_timestamp,
    updated_at      timestamptz        NOT NULL DEFAULT current_timestamp
);

CREATE INDEX ON user_credentials (user_id);

CREATE TRIGGER user_credentials_updated_at
    BEFORE UPDATE
    ON user_credentials
    FOR EACH ROW
EXECUTE PROCEDURE core.updated_at();
