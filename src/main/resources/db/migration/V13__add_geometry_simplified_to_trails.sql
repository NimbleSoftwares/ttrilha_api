ALTER TABLE trails ADD COLUMN geometry_simplified GEOGRAPHY(LineString, 4326);

-- INDEX
CREATE INDEX trails_geometry_simplified_idx
    ON trails
    USING GIST (geometry_simplified);
