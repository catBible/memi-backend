-- Hibernate + JDBC: text[] caused runtime errors on some Postgres drivers.
-- Store tags as TEXT containing a JSON array of strings, e.g. [] or ["a","b"].
ALTER TABLE tasks
	ALTER COLUMN tags TYPE text USING (
		CASE
			WHEN tags IS NULL THEN NULL
			ELSE array_to_json(tags)::text
		END
	);
