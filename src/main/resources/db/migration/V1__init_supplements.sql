-- Supplements: schema matches com.memi.lifeos.supplement.entity.Supplement (snake_case columns)
CREATE TABLE IF NOT EXISTS supplements (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    brand      VARCHAR(255),
    dosage     VARCHAR(255),
    form       VARCHAR(255),
    notes      VARCHAR(2000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_supplements_name_lower ON supplements (lower(name));
CREATE INDEX IF NOT EXISTS idx_supplements_brand_lower ON supplements (lower(brand)) WHERE brand IS NOT NULL;
