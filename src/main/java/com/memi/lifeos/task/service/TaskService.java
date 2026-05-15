package com.memi.lifeos.task.service;

import com.memi.lifeos.error.ResourceNotFoundException;
import com.memi.lifeos.task.dto.TaskMapper;
import com.memi.lifeos.task.dto.TaskPatchRequest;
import com.memi.lifeos.task.dto.TaskResponse;
import com.memi.lifeos.task.dto.TaskWriteRequest;
import com.memi.lifeos.task.dto.UpdateScheduleRequest;
import com.memi.lifeos.task.entity.Task;
import com.memi.lifeos.task.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class TaskService {

	private final TaskRepository repository;

	public TaskService(TaskRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<TaskResponse> getAllTasks() {
		return repository.findAllByDeletedAtIsNullOrderByCreatedAtDesc().stream()
			.map(TaskMapper::toResponse)
			.toList();
	}

	@Transactional(readOnly = true)
	public TaskResponse getTaskById(Long id) {
		return TaskMapper.toResponse(requireActive(id));
	}

	public TaskResponse createTask(TaskWriteRequest dto) {
		Task saved = repository.save(TaskMapper.toNewEntity(dto));
		return TaskMapper.toResponse(saved);
	}

	public TaskResponse updateTask(Long id, TaskPatchRequest patch) {
		Task t = requireActive(id);
		TaskMapper.applyPatch(patch, t);
		if (t.getTitle() == null || t.getTitle().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title cannot be empty");
		}
		return TaskMapper.toResponse(repository.save(t));
	}

	public void deleteTask(Long id) {
		Task t = requireActive(id);
		t.setDeletedAt(Instant.now());
		repository.save(t);
	}

	public TaskResponse updateSchedule(Long id, UpdateScheduleRequest dto) {
		if (dto.scheduledAt() == null && dto.endAt() == null && dto.dueAt() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one of scheduledAt, endAt, dueAt must be set");
		}
		Task t = requireActive(id);
		if (dto.scheduledAt() != null) {
			t.setScheduledAt(dto.scheduledAt());
		}
		if (dto.endAt() != null) {
			t.setEndAt(dto.endAt());
		}
		if (dto.dueAt() != null) {
			t.setDueAt(dto.dueAt());
		}
		return TaskMapper.toResponse(repository.save(t));
	}

	public TaskResponse startTimer(Long id) {
		Task t = requireActive(id);
		t.setTimerStartedAt(Instant.now());
		return TaskMapper.toResponse(repository.save(t));
	}

	public TaskResponse pauseTimer(Long id) {
		Task t = requireActive(id);
		accumulateRunningElapsed(t);
		return TaskMapper.toResponse(repository.save(t));
	}

	public TaskResponse stopTimer(Long id) {
		Task t = requireActive(id);
		accumulateRunningElapsed(t);
		t.setTimerStartedAt(null);
		int sec = t.getTimerElapsedSec() != null ? t.getTimerElapsedSec() : 0;
		t.setActualMinutes(sec / 60);
		return TaskMapper.toResponse(repository.save(t));
	}

	private static void accumulateRunningElapsed(Task t) {
		if (t.getTimerStartedAt() == null) {
			return;
		}
		long extra = Duration.between(t.getTimerStartedAt(), Instant.now()).getSeconds();
		int base = t.getTimerElapsedSec() != null ? t.getTimerElapsedSec() : 0;
		t.setTimerElapsedSec((int) Math.min(Integer.MAX_VALUE, (long) base + extra));
		t.setTimerStartedAt(null);
	}

	private Task requireActive(Long id) {
		Task t = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Task", id));
		if (t.getDeletedAt() != null) {
			throw new ResourceNotFoundException("Task", id);
		}
		return t;
	}
}
