package com.memi.lifeos.supplement.dto;

import java.time.Instant;

public record SupplementResponse(
	Integer id,
	String name,
	String brand,
	String dosage,
	int stockRemaining,
	String takenTimeSlot,
	Instant lastTakenAt
) {
}
