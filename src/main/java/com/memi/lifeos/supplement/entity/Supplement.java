package com.memi.lifeos.supplement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "supplements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/** Maps to PostgreSQL/Supabase {@code serial} (INTEGER), not BIGSERIAL. */
	private Integer id;

	@Column(nullable = false)
	private String name;
	private String brand;
	private String dosage;
	/** Remaining count (matches Supabase column stock_remaining). */
	@Column(name = "stock_remaining", nullable = false)
	@Builder.Default
	private int stockRemaining = 0;
	/** e.g. "Bedtime", "Noon" (matches taken_time_slot). */
	@Column(name = "taken_time_slot", length = 64)
	private String takenTimeSlot;
	/** Local time to take, 24h "HH:mm" (e.g. 12:00, 00:30). */
	@Column(name = "dose_time", length = 8)
	private String doseTime;
	/**
	 * Meal context: e.g. {@code before} = ก่อนอาหาร, {@code after} = หลังอาหาร; optional.
	 */
	@Column(name = "meal_timing", length = 32)
	private String mealTiming;
	@Column(name = "last_taken_at")
	private Instant lastTakenAt;
}
