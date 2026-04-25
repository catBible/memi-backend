-- รันบน PostgreSQL / Supabase SQL Editor ถ้ายังไม่ได้ deploy Flyway
-- ปรับ schema public/ชื่อ table ตาม environment

-- เวลาทาน แบบ 24h HH:mm
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS dose_time VARCHAR(8);

-- ก่อน / หลังอาหาร: เก็บเป็น 'before' หรือ 'after' (หรือ null)
ALTER TABLE supplements
	ADD COLUMN IF NOT EXISTS meal_timing VARCHAR(32);

-- อัปเดตตัวอย่าง (ปรับ id/ค่าให้ตรงข้อมูลจริง)
-- UPDATE supplements SET dose_time = '12:00', meal_timing = 'after' WHERE id = 1;
