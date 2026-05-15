package com.memi.lifeos.task.repository;

import com.memi.lifeos.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByDeletedAtIsNullOrderByCreatedAtDesc();
}
