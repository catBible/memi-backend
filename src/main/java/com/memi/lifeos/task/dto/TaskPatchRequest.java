package com.memi.lifeos.task.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/** Partial update (PATCH). Non-null fields overwrite; null fields are ignored. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskPatchRequest {

	@Size(max = 255)
	private String title;

	private String description;

	@Size(max = 20)
	private String status;

	@Min(0)
	@Max(32767)
	private Integer priority;

	private List<String> tags;

	@JsonProperty("scheduledAt")
	@JsonAlias("scheduled_at")
	private Instant scheduledAt;

	@JsonProperty("endAt")
	@JsonAlias("end_at")
	private Instant endAt;

	@JsonProperty("dueAt")
	@JsonAlias("due_at")
	private Instant dueAt;

	@JsonProperty("durationMinutes")
	@JsonAlias("duration_minutes")
	@Min(0)
	private Integer durationMinutes;

	@JsonProperty("actualMinutes")
	@JsonAlias("actual_minutes")
	@Min(0)
	private Integer actualMinutes;

	private String note;
}
