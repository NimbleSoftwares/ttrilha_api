CREATE TABLE IF NOT EXISTS user_blocks (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    blocker_id  UUID        NOT NULL,
    blocked_id  UUID        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- CONSTRAINTS
ALTER TABLE user_blocks ADD CONSTRAINT pk_user_blocks PRIMARY KEY (id);

ALTER TABLE user_blocks
    ADD CONSTRAINT fk_user_blocks_blocker FOREIGN KEY (blocker_id) REFERENCES users (id);

ALTER TABLE user_blocks
    ADD CONSTRAINT fk_user_blocks_blocked FOREIGN KEY (blocked_id) REFERENCES users (id);

-- Impede bloqueio duplicado entre os mesmos utilizadores
ALTER TABLE user_blocks
    ADD CONSTRAINT uk_user_blocks_blocker_blocked UNIQUE (blocker_id, blocked_id);

-- Impede auto-bloqueio
ALTER TABLE user_blocks
    ADD CONSTRAINT chk_user_blocks_no_self_block CHECK (blocker_id <> blocked_id);

-- INDEXES
CREATE INDEX idx_user_blocks_blocker_id ON user_blocks (blocker_id);

-- COMMENTS
COMMENT ON TABLE user_blocks IS 'Stores block relationships between users. Provides traceability of blocked users with timestamps';
COMMENT ON COLUMN user_blocks.blocker_id IS 'UUID of the user who performed the block';
COMMENT ON COLUMN user_blocks.blocked_id IS 'UUID of the user who was blocked';

