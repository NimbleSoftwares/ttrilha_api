ALTER TABLE users ADD COLUMN city VARCHAR(100);
ALTER TABLE users ADD COLUMN state VARCHAR(100);

-- COMMENTS
COMMENT ON COLUMN users.city IS 'City where the user is located, optional';
COMMENT ON COLUMN users.state IS 'State/province where the user is located, optional';

