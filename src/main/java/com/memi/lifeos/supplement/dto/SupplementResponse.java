package com.memi.lifeos.supplement.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/** JSON: camelCase. Aliases รองรับ snake_case ตอน bind (e.g. รายการจาก view อื่น) */
public record SupplementResponse(
	Integer id,
	String name,
	String brand,
	String dosage,
	int stockRemaining,
	@JsonProperty("takenTimeSlot")
	@JsonAlias("taken_time_slot")
	String takenTimeSlot,
	@JsonProperty("doseTime")
	@JsonAlias("dose_time")
	/** Local time to take, 24h "HH:mm"; may be null for legacy rows. */
	String doseTime,
	@JsonProperty("mealTiming")
	@JsonAlias("meal_timing")
	/**
	 * {@code before} / {@code after} (before or after food); may be null.
	 */
	String mealTiming,
	@JsonProperty("lastTakenAt")
	@JsonAlias("last_taken_at")
	Instant lastTakenAt
) {
}
