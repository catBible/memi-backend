-- Supplements: schema matches com.memi.lifeos.supplement.entity.Supplement (snake_case columns)
CREATE TABLE IF NOT EXISTS supplements (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    brand      VARCHAR(255),
    dosage     VARCHAR(255),
    form       VARCHAR(255),
    notes      VARCHAR(2000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- B-tree only (portable; functional/lower() indexes differ by vendor and can break on H2 / strict parsers)
CREATE INDEX IF NOT EXISTS idx_supplements_name ON supplements (name);
CREATE INDEX IF NOT EXISTS idx_supplements_brand ON supplements (brand);
