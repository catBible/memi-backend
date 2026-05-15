package com.memi.lifeos.task.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memi.lifeos.task.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TaskMapper {

	private static final ObjectMapper TAG_JSON = new ObjectMapper();
	private static final TypeReference<List<String>> TAG_LIST_TYPE = new TypeReference<>() {
	};

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
			.tagsJson(tagsToJson(tagList))
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
			tagsFromJson(t.getTagsJson()),
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
			target.setTagsJson(tagsToJson(p.getTags()));
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

	static List<String> tagsFromJson(String raw) {
		if (raw == null || raw.isBlank()) {
			return List.of();
		}
		String t = raw.trim();
		try {
			if (t.startsWith("[")) {
				return TAG_JSON.readValue(t, TAG_LIST_TYPE);
			}
		} catch (Exception ignored) {
			// fall through
		}
		// Legacy: plain comma-separated (should not happen after V7)
		if (t.contains(",")) {
			List<String> out = new ArrayList<>();
			for (String s : t.split(",")) {
				String x = s.trim();
				if (!x.isEmpty()) {
					out.add(x);
				}
			}
			return out;
		}
		return t.isEmpty() ? List.of() : List.of(t);
	}

	static String tagsToJson(List<String> tags) {
		try {
			List<String> list = tags == null ? List.of() : tags;
			return TAG_JSON.writeValueAsString(list);
		} catch (Exception e) {
			return "[]";
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
