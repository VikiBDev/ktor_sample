create table session
(
    id         uuid PRIMARY KEY,
    user_id    uuid        NOT NULL REFERENCES "user"."user" (id),
    created_at timestamptz NOT NULL DEFAULT current_timestamp,
    updated_at timestamptz NOT NULL DEFAULT current_timestamp,
    expires_at timestamptz NOT NULL
);

CREATE INDEX ON session (user_id, expires_at);

CREATE TRIGGER session_updated_at
    BEFORE UPDATE
    ON session
    FOR EACH ROW
EXECUTE PROCEDURE core.updated_at();
