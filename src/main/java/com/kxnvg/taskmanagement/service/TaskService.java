package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.dto.TaskDto;
import com.kxnvg.taskmanagement.entity.Task;
import com.kxnvg.taskmanagement.mapper.TaskMapper;
import com.kxnvg.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public TaskDto getTask(Long taskId) {
        Task task = takeTaskFromDB(taskId);
        log.info("Task with id={} was taken from DB successfully", taskId);
        return taskMapper.toDto(task);
    }

    @Transactional
    public Long createTask(TaskDto taskDto) {
        userService.takeUserFromDB(taskDto.getAuthorId());
        Task savedTask = taskRepository.save(taskMapper.toEntity(taskDto));
        log.info("Task with title={} was saved in DB successfully", taskDto.getTitle());
        return savedTask.getId();
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDto) {
        userService.checkCorrectnessUserAction(taskDto.getAuthorId());

        Task task = takeTaskFromDB(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setContent(taskDto.getContent());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());
        task.setExecutor(userService.takeUserFromDB(taskDto.getExecutorId()));

        log.info("Task with id={} was updated successfully", task.getId());
        return taskMapper.toDto(task);
    }

    @Transactional
    public Boolean deleteTask(Long taskId) {
        Optional<Task> maybeTask = taskRepository.findById(taskId);
        if (maybeTask.isPresent()) {
            userService.checkCorrectnessUserAction(maybeTask.get().getAuthor().getId());
            taskRepository.deleteById(taskId);
            log.info("Task with id={} was deleted successfully", taskId);
            return true;
        }
        log.info("Task with id={} is not found");
        return false;
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllAuthorTask(Long authorId) {
        userService.takeUserFromDB(authorId);
        List<Task> allTasks = taskRepository.findByAuthorId(authorId);
        log.info("List of author's task is taken from DB, userId={}", authorId);
        return allTasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllExecutorTask(Long executorId) {
        userService.takeUserFromDB(executorId);
        List<Task> allTasks = taskRepository.findByExecutorId(executorId);
        log.info("List of executor's task is taken from DB, userId={}", executorId);
        return allTasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    private Task takeTaskFromDB(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task with id=%d is not found", taskId)));
    }
}
