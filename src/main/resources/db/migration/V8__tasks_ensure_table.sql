-- Safety net: if earlier migrations did not create `tasks` (e.g. partial deploy), create it here.
-- If `tasks` already exists (from V6 or manual SQL), this is a no-op.
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
