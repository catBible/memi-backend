package com.memi.lifeos.supplement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Create or full replace (PUT) body; partial PATCH can reuse later.
 */
public record SupplementWriteRequest(
	@NotBlank @Size(max = 255) String name,
	@Size(max = 255) String brand,
	@Size(max = 255) String dosage,
	@Size(max = 255) String form,
	@Size(max = 2000) String notes
) {
}
