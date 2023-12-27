package com.kxnvg.taskmanagement.service;

import com.kxnvg.taskmanagement.dto.TaskDto;
import com.kxnvg.taskmanagement.entity.Task;
import com.kxnvg.taskmanagement.entity.User;
import com.kxnvg.taskmanagement.entity.enums.TaskStatus;
import com.kxnvg.taskmanagement.exception.IncorrectUserActionException;
import com.kxnvg.taskmanagement.mapper.TaskMapperImpl;
import com.kxnvg.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final long TASK_ID = 1L;
    private static final long USER_ID = 1L;
    private Task task;
    private TaskDto taskDto;
    private User user;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Spy
    private TaskMapperImpl taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void initData() {
        user = User.builder()
                .id(USER_ID)
                .build();
        task = Task.builder()
                .id(TASK_ID)
                .title("Test")
                .content("Test")
                .author(user)
                .comments(new ArrayList<>())
                .build();
        taskDto = TaskDto.builder()
                .id(TASK_ID)
                .title("Test")
                .content("Test")
                .authorId(USER_ID)
                .commentsId(new ArrayList<>())
                .build();
    }

    @Test
    void testGetTaskWithoutEntity() {
        when(taskRepository.findById(TASK_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> taskService.getTask(TASK_ID));
    }

    @Test
    void testGetTask() {
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));
        TaskDto actualTaskDto = taskService.getTask(TASK_ID);
        assertEquals(taskDto, actualTaskDto);
    }

    @Test
    void testCreateTask() {
        task.setComments(null);
        when(taskRepository.save(task)).thenReturn(task);
        Long actualTaskId = taskService.createTask(taskDto);
        assertEquals(TASK_ID, actualTaskId);
    }

    @Test
    void testUpdateTaskWithIncorrectUserAction() {
        when(userService.checkCorrectnessUserAction(USER_ID)).thenThrow(IncorrectUserActionException.class);
        assertThrows(IncorrectUserActionException.class, () -> taskService.updateTask(taskDto));
    }

    @Test
    void testUpdateTask() {
        taskDto.setExecutorId(USER_ID);
        taskDto.setStatus(TaskStatus.CLOSE);
        when(userService.checkCorrectnessUserAction(USER_ID)).thenReturn(user);
        when(userService.takeUserFromDB(USER_ID)).thenReturn(user);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));

        TaskDto actualTaskDto = taskService.updateTask(taskDto);
        assertEquals(taskDto, actualTaskDto);

    }

    @Test
    void testDeleteTaskWithoutTaskInDB() {
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());
        Boolean actualResult = taskService.deleteTask(TASK_ID);
        assertEquals(false, actualResult);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));
        when(userService.checkCorrectnessUserAction(USER_ID)).thenReturn(user);
        Boolean actualResult = taskService.deleteTask(TASK_ID);
        assertEquals(true, actualResult);
    }

    @Test
    void testGetAllAuthorTask() {
        when(userService.takeUserFromDB(USER_ID)).thenReturn(user);
        when(taskRepository.findByAuthorId(USER_ID)).thenReturn(List.of(task));
        List<TaskDto> actualList = taskService.getAllAuthorTask(USER_ID);
        List<TaskDto> expectedList = List.of(taskDto);
        assertEquals(expectedList, actualList);
    }

    @Test
    void testGetAllExecutorTask() {
        when(userService.takeUserFromDB(USER_ID)).thenReturn(user);
        when(taskRepository.findByAuthorId(USER_ID)).thenReturn(List.of(task));
        List<TaskDto> actualList = taskService.getAllAuthorTask(USER_ID);
        List<TaskDto> expectedList = List.of(taskDto);
        assertEquals(expectedList, actualList);
    }
}