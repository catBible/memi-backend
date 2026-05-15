package com.memi.lifeos.task.api;

import com.memi.lifeos.task.dto.TaskPatchRequest;
import com.memi.lifeos.task.dto.TaskResponse;
import com.memi.lifeos.task.dto.TaskWriteRequest;
import com.memi.lifeos.task.dto.UpdateScheduleRequest;
import com.memi.lifeos.task.service.TaskService;
import com.memi.lifeos.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	@Operation(summary = "List all active tasks")
	public ResponseEntity<ApiResponse<List<TaskResponse>>> list() {
		return ResponseEntity.ok(ApiResponse.ok(taskService.getAllTasks()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get one task by id")
	public ResponseEntity<ApiResponse<TaskResponse>> get(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.getTaskById(id)));
	}

	@PostMapping
	@Operation(summary = "Create task", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> create(@Valid @RequestBody TaskWriteRequest body) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(taskService.createTask(body)));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Partial update task", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> patch(
		@PathVariable Long id,
		@Valid @RequestBody TaskPatchRequest body) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.updateTask(id, body)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Soft-delete task", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.ok(ApiResponse.okMessage("Deleted"));
	}

	@PatchMapping("/{id}/schedule")
	@Operation(summary = "Update schedule fields", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> schedule(
		@PathVariable Long id,
		@Valid @RequestBody UpdateScheduleRequest body) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.updateSchedule(id, body)));
	}

	@PatchMapping("/{id}/timer/start")
	@Operation(summary = "Start timer", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> timerStart(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.startTimer(id)));
	}

	@PatchMapping("/{id}/timer/pause")
	@Operation(summary = "Pause timer", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> timerPause(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.pauseTimer(id)));
	}

	@PatchMapping("/{id}/timer/stop")
	@Operation(summary = "Stop timer", security = @SecurityRequirement(name = "X-API-Key"))
	public ResponseEntity<ApiResponse<TaskResponse>> timerStop(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.ok(taskService.stopTimer(id)));
	}
}
