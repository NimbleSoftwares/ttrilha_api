-- Add invite_id and status to expedition_members
-- Existing rows (expedition creators) get ACCEPTED status and a generated UUID

ALTER TABLE expedition_members
    ADD COLUMN invite_id UUID NOT NULL DEFAULT gen_random_uuid(),
    ADD COLUMN status     VARCHAR(20) NOT NULL DEFAULT 'ACCEPTED';

-- Unique constraint on invite_id so it can be used as an opaque token
ALTER TABLE expedition_members
    ADD CONSTRAINT uq_expedition_members_invite_id UNIQUE (invite_id);

CREATE INDEX idx_expedition_members_status  ON expedition_members(expedition_id, status);
CREATE INDEX idx_expedition_members_invite  ON expedition_members(invite_id);

