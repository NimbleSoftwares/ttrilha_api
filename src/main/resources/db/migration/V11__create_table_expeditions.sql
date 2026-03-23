CREATE TABLE IF NOT EXISTS expeditions (
    id                  UUID        NOT NULL DEFAULT gen_random_uuid(),
    title               VARCHAR(255) NOT NULL,
    trail_id            UUID        NOT NULL,
    start_date          DATE        NOT NULL,
    end_date            DATE        NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
    created_by_user_id  UUID        NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_expeditions PRIMARY KEY (id),
    CONSTRAINT fk_expeditions_trail
        FOREIGN KEY (trail_id) REFERENCES trails(id),
    CONSTRAINT fk_expeditions_creator
        FOREIGN KEY (created_by_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS expedition_members (
    expedition_id   UUID NOT NULL,
    user_id         UUID NOT NULL,

    CONSTRAINT pk_expedition_members PRIMARY KEY (expedition_id, user_id),
    CONSTRAINT fk_expedition_members_expedition
        FOREIGN KEY (expedition_id) REFERENCES expeditions(id) ON DELETE CASCADE,
    CONSTRAINT fk_expedition_members_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_expeditions_created_by ON expeditions(created_by_user_id);
CREATE INDEX idx_expedition_members_user ON expedition_members(user_id);

