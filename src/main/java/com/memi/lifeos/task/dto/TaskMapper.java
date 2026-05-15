package com.memi.lifeos.task.dto;

import com.memi.lifeos.task.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TaskMapper {

	private TaskMapper() {
	}

	public static Task toNewEntity(TaskWriteRequest w) {
		Objects.requireNonNull(w, "w");
		List<String> tagList = w.tags() == null ? new ArrayList<>() : new ArrayList<>(w.tags());
		return Task.builder()
			.title(w.title().trim())
			.description(trimOrNull(w.description()))
			.status(normalizeStatus(w.status()))
			.priority(w.priority() != null ? w.priority() : 2)
			.tags(tagList)
			.scheduledAt(w.scheduledAt())
			.endAt(w.endAt())
			.dueAt(w.dueAt())
			.durationMinutes(w.durationMinutes())
			.actualMinutes(w.actualMinutes())
			.note(trimOrNull(w.note()))
			.timerElapsedSec(0)
			.build();
	}

	public static TaskResponse toResponse(Task t) {
		return new TaskResponse(
			t.getId(),
			t.getTitle(),
			t.getDescription(),
			t.getStatus(),
			t.getPriority(),
			t.getTags() == null ? List.of() : List.copyOf(t.getTags()),
			t.getScheduledAt(),
			t.getEndAt(),
			t.getDueAt(),
			t.getDurationMinutes(),
			t.getActualMinutes(),
			t.getTimerStartedAt(),
			t.getTimerElapsedSec(),
			t.getNote(),
			t.getCreatedAt(),
			t.getUpdatedAt(),
			t.getDeletedAt()
		);
	}

	public static void applyPatch(TaskPatchRequest p, Task target) {
		Objects.requireNonNull(p, "p");
		Objects.requireNonNull(target, "target");
		if (p.getTitle() != null) {
			String t = p.getTitle().trim();
			if (!t.isEmpty()) {
				target.setTitle(t);
			}
		}
		if (p.getDescription() != null) {
			target.setDescription(trimOrNull(p.getDescription()));
		}
		if (p.getStatus() != null) {
			target.setStatus(normalizeStatus(p.getStatus()));
		}
		if (p.getPriority() != null) {
			target.setPriority(p.getPriority());
		}
		if (p.getTags() != null) {
			target.setTags(new ArrayList<>(p.getTags()));
		}
		if (p.getScheduledAt() != null) {
			target.setScheduledAt(p.getScheduledAt());
		}
		if (p.getEndAt() != null) {
			target.setEndAt(p.getEndAt());
		}
		if (p.getDueAt() != null) {
			target.setDueAt(p.getDueAt());
		}
		if (p.getDurationMinutes() != null) {
			target.setDurationMinutes(p.getDurationMinutes());
		}
		if (p.getActualMinutes() != null) {
			target.setActualMinutes(p.getActualMinutes());
		}
		if (p.getNote() != null) {
			target.setNote(trimOrNull(p.getNote()));
		}
	}

	private static String trimOrNull(String v) {
		if (v == null) {
			return null;
		}
		String t = v.trim();
		return t.isEmpty() ? null : t;
	}

	private static String normalizeStatus(String s) {
		if (s == null || s.isBlank()) {
			return "TODO";
		}
		return s.trim().toUpperCase();
	}
}
