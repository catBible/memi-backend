-- Default rows for Postgres (Railway, etc.); only when the table has no rows yet.
INSERT INTO supplements (name, brand, dosage, form, notes, created_at, updated_at)
SELECT v.name, v.brand, v.dosage, v.form, v.notes, now(), now()
FROM (VALUES
	('Vitamin D3', 'Nordic Naturals', '2000 IU', 'softgel', 'Take with breakfast'),
	('Omega-3 (EPA/DHA)', 'Carlson', '1000 mg', 'softgel', NULL::character varying(2000))
) AS v(name, brand, dosage, form, notes)
WHERE NOT EXISTS (SELECT 1 FROM supplements);
