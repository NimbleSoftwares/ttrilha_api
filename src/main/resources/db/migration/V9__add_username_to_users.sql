ALTER TABLE users ADD COLUMN username VARCHAR(30);

-- Unique constraint allowing multiple NULLs (partial index)
CREATE UNIQUE INDEX IDX_USERS_USERNAME_ACTIVE ON users (username)
    WHERE username IS NOT NULL;

-- COMMENTS
COMMENT ON COLUMN users.username IS 'Optional unique username chosen by the user (e.g. ookmpx, cool_user-99). Used for friend search and displayed on media in memory box.';
