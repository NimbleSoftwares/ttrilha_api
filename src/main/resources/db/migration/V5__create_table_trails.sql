CREATE TABLE IF NOT EXISTS trails (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    osm_id BIGINT,
    created_by_user_id UUID,
    name VARCHAR(255),
    difficulty VARCHAR(50),
    tags JSONB,
    geometry GEOGRAPHY(LineString, 4326),
    distance_meters DOUBLE PRECISION GENERATED ALWAYS AS (ST_Length(geometry)) STORED,
    elevation_gain DOUBLE PRECISION,

    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- CONSTRAINTS
ALTER TABLE trails ADD PRIMARY KEY (id);

ALTER TABLE trails
    ADD CONSTRAINT uk_trails_osm_id UNIQUE (osm_id);

-- INDEXES
CREATE INDEX trails_geometry_idx
    ON trails
        USING GIST (geometry);