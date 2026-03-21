CREATE TABLE IF NOT EXISTS friendships (
    id               UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id          UUID        NOT NULL,
    friend_id        UUID        NOT NULL,
    solicitation_id  UUID        NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- CONSTRAINTS
ALTER TABLE friendships ADD CONSTRAINT pk_friendships PRIMARY KEY (id);

ALTER TABLE friendships
    ADD CONSTRAINT fk_friendship_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE friendships
    ADD CONSTRAINT fk_friendship_friend FOREIGN KEY (friend_id) REFERENCES users (id);

ALTER TABLE friendships
    ADD CONSTRAINT fk_friendship_solicitation FOREIGN KEY (solicitation_id) REFERENCES friendship_solicitations (id);

-- Impede amizade duplicada entre os mesmos usuarios
ALTER TABLE friendships
    ADD CONSTRAINT uk_friendship_user_friend UNIQUE (user_id, friend_id);

-- INDEXES
CREATE INDEX idx_friendships_user_id ON friendships (user_id);

-- COMMENTS
COMMENT ON TABLE friendships IS 'Stores confirmed friendships between users. Each accepted solicitation generates two rows (bidirectional)';
COMMENT ON COLUMN friendships.user_id IS 'UUID of the user';
COMMENT ON COLUMN friendships.friend_id IS 'UUID of the friend';
COMMENT ON COLUMN friendships.solicitation_id IS 'Reference to the solicitation that originated this friendship';
