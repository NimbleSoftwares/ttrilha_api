CREATE TABLE IF NOT EXISTS user_identities(
   user_id UUID NOT NULL,
   provider oauth_provider NOT NULL DEFAULT 'GOOGLE',
   provider_user_id VARCHAR(255) NOT NULL,
   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   deleted_at TIMESTAMPTZ
);

-- CONSTRAINTS: PRIMARY KEY, FOREIGN KEY
ALTER TABLE user_identities ADD CONSTRAINT PK_USER_IDENTITIES
    PRIMARY KEY (user_id, provider, provider_user_id);

ALTER TABLE user_identities ADD CONSTRAINT FK_USER_IDENTITIES_USERS
    FOREIGN KEY  (user_id)
    REFERENCES users(id) ON DELETE CASCADE;

-- INDEXES:
CREATE INDEX IDX_USER_IDENTITIES_ACTIVE ON user_identities (user_id)
    WHERE deleted_at IS NULL;

-- COMMENTS:
COMMENT ON TABLE user_identities IS
    'User identities, used for OAuth2, stores the internal ttrilha user_id, the provider and the provider user id';

COMMENT ON COLUMN user_identities.user_id IS 'The internal ttrilha user id';
COMMENT ON COLUMN user_identities.provider IS 'The OAuth2 provider (ex: Auth0)';
COMMENT ON COLUMN user_identities.provider_user_id IS 'The OAuth2 provider user id (sub claim)';
COMMENT ON COLUMN user_identities.created_at IS 'The creation date';
COMMENT ON COLUMN user_identities.updated_at IS 'The last update date';
COMMENT ON COLUMN user_identities.deleted_at IS 'User deletion date, optional, used to audit soft deletes';
