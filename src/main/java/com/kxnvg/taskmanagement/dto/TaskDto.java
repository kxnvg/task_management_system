package com.kxnvg.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kxnvg.taskmanagement.entity.enums.TaskPriority;
import com.kxnvg.taskmanagement.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private Long id;

    @NotBlank(message = "Title can't be empty")
    @Size(max = 128, message = "Title can't be more than 128 symbols")
    private String title;

    @Size(max = 4096, message = "Content can't be more than 4096 symbols")
    private String content;

    @NotNull(message = "Status can't be null")
    private TaskStatus status;

    private TaskPriority priority;

    @NotNull(message = "Task can't be without author")
    private Long authorId;

    private Long executorId;

    private List<Long> commentsId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
