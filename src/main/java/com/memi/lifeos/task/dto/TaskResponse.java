package com.memi.lifeos.task.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

/** JSON: camelCase; aliases for snake_case. */
public record TaskResponse(
	Long id,
	String title,
	String description,
	String status,
	Integer priority,
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
	Integer durationMinutes,
	@JsonProperty("actualMinutes")
	@JsonAlias("actual_minutes")
	Integer actualMinutes,
	@JsonProperty("timerStartedAt")
	@JsonAlias("timer_started_at")
	Instant timerStartedAt,
	@JsonProperty("timerElapsedSec")
	@JsonAlias("timer_elapsed_sec")
	Integer timerElapsedSec,
	String note,
	@JsonProperty("createdAt")
	@JsonAlias("created_at")
	Instant createdAt,
	@JsonProperty("updatedAt")
	@JsonAlias("updated_at")
	Instant updatedAt,
	@JsonProperty("deletedAt")
	@JsonAlias("deleted_at")
	Instant deletedAt
) {
}
