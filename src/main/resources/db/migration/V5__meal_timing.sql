-- ก่อน/หลังอาหาร: ค่าแนะนำ 'before' | 'after' (nullable)
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS meal_timing VARCHAR(32);
