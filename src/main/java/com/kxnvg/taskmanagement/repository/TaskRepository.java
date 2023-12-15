package com.kxnvg.taskmanagement.repository;

import com.kxnvg.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
