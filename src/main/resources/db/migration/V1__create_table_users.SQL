CREATE TABLE IF NOT EXISTS users (
	id UUID NOT NULL DEFAULT gen_random_uuid(),
    email VARCHAR(254) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(70) NOT NULL,
    avatar_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- CONSTRAINTS: PRIMARY KEY
ALTER TABLE users ADD CONSTRAINT PK_USERS PRIMARY KEY (id);

-- INDEXES:
CREATE UNIQUE INDEX IDX_USERS_EMAIL_ACTIVE ON users (email)
    WHERE deleted_at IS NULL;
CREATE INDEX IDX_USERS_ACTIVE ON users (id)
    WHERE deleted_at IS NULL;

-- COMMENTS:
COMMENT ON TABLE users IS 'User table, stores internal user data';

COMMENT ON COLUMN users.id IS 'Internal User id';
COMMENT ON COLUMN users.email IS 'User email';
COMMENT ON COLUMN users.display_name IS 'User display name (Usually the full name)';
COMMENT ON COLUMN users.first_name IS 'User first name';
COMMENT ON COLUMN users.last_name IS 'User last name';
COMMENT ON COLUMN users.avatar_url IS 'User avatar url';
COMMENT ON COLUMN users.created_at IS 'User creation date';
COMMENT ON COLUMN users.updated_at IS 'User last update date';
COMMENT ON COLUMN users.deleted_at IS 'User deletion date, optional, used to audit soft deletes';