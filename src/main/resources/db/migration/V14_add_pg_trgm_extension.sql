CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE INDEX trails_name_trgm_idx
    ON trails
    USING GIN (name gin_trgm_ops);