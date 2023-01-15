CREATE TABLE store_reward_transaction
(
    points_transaction_id uuid NOT NULL REFERENCES point.points_transaction ON DELETE CASCADE,
    store_reward_id       uuid NOT NULL REFERENCES store.reward
);
