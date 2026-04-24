package com.memi.lifeos.supplement.dto;

import java.time.Instant;

public record SupplementResponse(
	Long id,
	String name,
	String brand,
	String dosage,
	String form,
	String notes,
	Instant createdAt,
	Instant updatedAt
) {
}
