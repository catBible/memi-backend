-- Align supplements with app (matches Supabase lifeos: stock, time slot, last taken).
-- No-op for tables already in this shape; migrates from legacy V1 (form, notes, timestamps) when present.
-- Numbered V3: V2 was previously used and removed; avoid duplicate Flyway version conflicts.
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS stock_remaining INTEGER;
UPDATE supplements SET stock_remaining = 0 WHERE stock_remaining IS NULL;
ALTER TABLE supplements
	ALTER COLUMN stock_remaining SET DEFAULT 0;
ALTER TABLE supplements
	ALTER COLUMN stock_remaining SET NOT NULL;

ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS taken_time_slot VARCHAR(64);
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS last_taken_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE supplements DROP COLUMN IF EXISTS form;
ALTER TABLE supplements DROP COLUMN IF EXISTS notes;
ALTER TABLE supplements DROP COLUMN IF EXISTS created_at;
ALTER TABLE supplements DROP COLUMN IF EXISTS updated_at;
