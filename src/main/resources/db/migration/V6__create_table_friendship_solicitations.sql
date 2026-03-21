CREATE TABLE IF NOT EXISTS friendship_solicitations (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    requester_id UUID        NOT NULL,
    addressee_id UUID        NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- CONSTRAINTS
ALTER TABLE friendship_solicitations ADD CONSTRAINT pk_friendship_solicitations PRIMARY KEY (id);

ALTER TABLE friendship_solicitations
    ADD CONSTRAINT fk_solicitation_requester FOREIGN KEY (requester_id) REFERENCES users (id);

ALTER TABLE friendship_solicitations
    ADD CONSTRAINT fk_solicitation_addressee FOREIGN KEY (addressee_id) REFERENCES users (id);

ALTER TABLE friendship_solicitations
    ADD CONSTRAINT chk_solicitation_status CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED'));

-- Evitar solicitacoes duplicadas pendentes entre os mesmos usuarios
ALTER TABLE friendship_solicitations
    ADD CONSTRAINT uk_solicitation_pending UNIQUE (requester_id, addressee_id, status);

-- INDEXES
CREATE INDEX idx_solicitations_requester_id ON friendship_solicitations (requester_id);
CREATE INDEX idx_solicitations_addressee_id ON friendship_solicitations (addressee_id);

-- TRIGGER: atualiza updated_at automaticamente
CREATE OR REPLACE FUNCTION fn_update_solicitation_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_solicitation_updated_at
    BEFORE UPDATE ON friendship_solicitations
    FOR EACH ROW
EXECUTE PROCEDURE fn_update_solicitation_updated_at();

-- COMMENTS
COMMENT ON TABLE friendship_solicitations IS 'Stores friendship invitations sent between users';
COMMENT ON COLUMN friendship_solicitations.requester_id IS 'UUID of the user who sent the invitation';
COMMENT ON COLUMN friendship_solicitations.addressee_id IS 'UUID of the user who received the invitation';
COMMENT ON COLUMN friendship_solicitations.status IS 'Current status: PENDING, ACCEPTED, REJECTED, CANCELLED';
