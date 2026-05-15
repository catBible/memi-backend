# Manual SQL (Postgres)

## `create_tasks_table_postgres.sql`

Run in **Supabase → SQL Editor** or **Railway → Postgres → Query** when:

- The `tasks` table was never created, and
- You confirmed Flyway is not applying migrations (see checklist below).

`tags` is stored as **TEXT** containing a JSON array (default `[]`), matching the current Spring entity.

## If the table should be created by Flyway instead

1. On **Railway** (backend service): open **Deploy Logs** and search for `Flyway` / `Migrating schema`.
2. Ensure env does **not** set `SPRING_FLYWAY_ENABLED=false`.
3. Confirm `DATABASE_URL` points to the **same** database you inspect in the SQL UI.
4. Redeploy after pushing the latest `memi-backend` (includes `V6`–`V8` migrations; `V8` is a safety `CREATE TABLE IF NOT EXISTS` with `tags` as JSON text).

If Flyway reports a failed migration, fix the DB or use `flyway repair` / clear the failed row in `flyway_schema_history` per Flyway docs, then redeploy.
