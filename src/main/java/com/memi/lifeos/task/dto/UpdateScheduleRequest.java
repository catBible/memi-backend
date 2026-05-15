package com.memi.lifeos.task.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UpdateScheduleRequest(
	@JsonProperty("scheduledAt")
	@JsonAlias("scheduled_at")
	Instant scheduledAt,
	@JsonProperty("endAt")
	@JsonAlias("end_at")
	Instant endAt,
	@JsonProperty("dueAt")
	@JsonAlias("due_at")
	Instant dueAt
) {
}
