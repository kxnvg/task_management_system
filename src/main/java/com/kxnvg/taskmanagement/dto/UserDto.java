package com.kxnvg.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kxnvg.taskmanagement.entity.Comment;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
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
public class UserDto {

    @NotNull(message = "User's id can't be null")
    private Long id;

    @NotBlank(message = "First name can't be empty")
    @Size(max = 128, message = "First name can't be more than 128 symbols")
    private String firstname;

    @NotBlank(message = "Last name can't be empty")
    @Size(max = 128, message = "Last name can't be more than 128 symbols")
    private String lastname;

    private String email;

    private String password;

    private UserRole role;

    private List<Long> createdTasksId;

    private List<Long> executableTasksId;

    private List<Long> commentsId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAT;
}
