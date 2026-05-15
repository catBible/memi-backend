-- ---------------------------------------------------------------------------
-- Manual: create `tasks` on Postgres (Supabase / Railway SQL editor / psql)
-- Use when the API never created the table (Flyway did not run or failed).
-- After this, redeploy the Spring app; Flyway V6/V7 should no-op or only fix tags type.
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS tasks (
	id                 BIGSERIAL PRIMARY KEY,
	title              VARCHAR(255) NOT NULL,
	description        TEXT,
	status             VARCHAR(20)  NOT NULL DEFAULT 'TODO',
	priority           SMALLINT     NOT NULL DEFAULT 2,
	tags               TEXT NOT NULL DEFAULT '[]',
	scheduled_at       TIMESTAMPTZ,
	end_at             TIMESTAMPTZ,
	due_at             TIMESTAMPTZ,
	duration_minutes   INTEGER,
	actual_minutes     INTEGER,
	timer_started_at   TIMESTAMPTZ,
	timer_elapsed_sec  INTEGER NOT NULL DEFAULT 0,
	note               TEXT,
	created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	deleted_at         TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_tasks_due_at ON tasks(due_at) WHERE deleted_at IS NULL;
