package com.memi.lifeos.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, length = 20)
	@Builder.Default
	private String status = "TODO";

	@Column(nullable = false)
	@Builder.Default
	private Integer priority = 2;

	/**
	 * JSON array of strings, e.g. {@code []} or {@code ["home","urgent"]}.
	 * Stored as TEXT (not native PG {@code text[]}) for JDBC compatibility.
	 */
	@Column(name = "tags", columnDefinition = "TEXT")
	private String tagsJson;

	@Column(name = "scheduled_at")
	private Instant scheduledAt;

	@Column(name = "end_at")
	private Instant endAt;

	@Column(name = "due_at")
	private Instant dueAt;

	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	@Column(name = "actual_minutes")
	private Integer actualMinutes;

	@Column(name = "timer_started_at")
	private Instant timerStartedAt;

	@Column(name = "timer_elapsed_sec", nullable = false)
	@Builder.Default
	private Integer timerElapsedSec = 0;

	@Column(columnDefinition = "TEXT")
	private String note;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@PrePersist
	void prePersist() {
		Instant now = Instant.now();
		if (createdAt == null) {
			createdAt = now;
		}
		if (updatedAt == null) {
			updatedAt = now;
		}
		if (tagsJson == null || tagsJson.isBlank()) {
			tagsJson = "[]";
		}
		if (timerElapsedSec == null) {
			timerElapsedSec = 0;
		}
		if (priority == null) {
			priority = 2;
		}
		if (status == null || status.isBlank()) {
			status = "TODO";
		}
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = Instant.now();
	}
}
