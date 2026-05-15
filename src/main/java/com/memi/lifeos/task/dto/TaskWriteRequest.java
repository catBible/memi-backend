package com.memi.lifeos.task.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

/** Create body (POST). */
public record TaskWriteRequest(
	@NotBlank @Size(max = 255) String title,
	String description,
	@Size(max = 20) String status,
	@Min(0) @Max(32767) Integer priority,
	List<String> tags,
	@JsonProperty("scheduledAt")
	@JsonAlias("scheduled_at")
	Instant scheduledAt,
	@JsonProperty("endAt")
	@JsonAlias("end_at")
	Instant endAt,
	@JsonProperty("dueAt")
	@JsonAlias("due_at")
	Instant dueAt,
	@JsonProperty("durationMinutes")
	@JsonAlias("duration_minutes")
	@Min(0)
	Integer durationMinutes,
	@JsonProperty("actualMinutes")
	@JsonAlias("actual_minutes")
	@Min(0)
	Integer actualMinutes,
	String note
) {
}
