package com.kxnvg.taskmanagement.repository;

import com.kxnvg.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthorId(Long authorId);

    List<Task> findByExecutorId(Long executorId);
}
