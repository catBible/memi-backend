-- Convert tags from PostgreSQL text[] to TEXT (JSON array string) when needed.
-- Skips if tasks table or column missing, or if tags is already plain text (e.g. manual DDL).
DO $$
BEGIN
	IF EXISTS (
		SELECT 1
		FROM information_schema.columns
		WHERE table_schema = 'public'
			AND table_name = 'tasks'
			AND column_name = 'tags'
			AND udt_name = '_text'
	) THEN
		ALTER TABLE tasks
			ALTER COLUMN tags TYPE text USING (
				CASE
					WHEN tags IS NULL THEN NULL
					ELSE array_to_json(tags)::text
				END
			);
	END IF;
END $$;
