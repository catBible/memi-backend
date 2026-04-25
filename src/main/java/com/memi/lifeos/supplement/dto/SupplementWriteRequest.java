package com.memi.lifeos.supplement.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Create or full replace (PUT) body. รองรับทั้ง camelCase และ alias แบบ snake_case.
 */
public record SupplementWriteRequest(
	@NotBlank @Size(max = 255) String name,
	@Size(max = 255) String brand,
	@Size(max = 255) String dosage,
	@Min(0) int stockRemaining,
	@JsonProperty("takenTimeSlot")
	@JsonAlias("taken_time_slot")
	@Size(max = 64) String takenTimeSlot,
	/** 24h "HH:mm" (e.g. 12:00, 00:30); optional. */
	@JsonProperty("doseTime")
	@JsonAlias("dose_time")
	@Size(max = 8) String doseTime,
	/** "before" | "after" (before/after meal); optional. */
	@JsonProperty("mealTiming")
	@JsonAlias("meal_timing")
	@Size(max = 32) String mealTiming
) {
}
