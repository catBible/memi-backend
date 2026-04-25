-- Local time-of-day to take the dose (24h "HH:mm"). Optional; use with taken_time_slot for stack grouping.
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS dose_time VARCHAR(8);
