package com.kxnvg.taskmanagement.controller;

import com.kxnvg.taskmanagement.dto.TaskDto;
import com.kxnvg.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("api/v1/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public TaskDto getTask(@PathVariable Long taskId) {
        log.info("Received request to get task with id={}", taskId);
        return taskService.getTask(taskId);
    }

    @PostMapping
    public Long createTask(@RequestBody TaskDto taskDto) {
        log.info("Received request to create task with title={}", taskDto.getTitle());
        return taskService.createTask(taskDto);
    }

    @PutMapping
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        log.info("Received request to update task with id={}", taskDto.getId());
        return taskService.updateTask(taskDto);
    }

    @DeleteMapping("/{taskId}")
    public Boolean deleteTask(@PathVariable Long taskId) {
        log.info("Received request to delete task with id={}", taskId);
        return taskService.deleteTask(taskId);
    }

    @GetMapping("/author/{authorId}")
    public List<TaskDto> getAllAuthorTask(@PathVariable Long authorId) {
        log.info("Received request to get all author's tasks, with userId={}", authorId);
        return taskService.getAllAuthorTask(authorId);
    }

    @GetMapping("/executor/{executorId}")
    public List<TaskDto> getAllExecutorTask(@PathVariable Long executorId) {
        log.info("Received request to get all executor's tasks, with userId={}", executorId);
        return taskService.getAllExecutorTask(executorId);
    }
}
