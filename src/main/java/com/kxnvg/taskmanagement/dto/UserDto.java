package com.kxnvg.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kxnvg.taskmanagement.entity.Comment;
import com.kxnvg.taskmanagement.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Email can't be empty")
    @Size(max = 128, message = "Email can't be more than 128 symbols")
    private String email;

    @NotBlank(message = "Username can't be empty")
    @Size(max = 128, message = "Username can't be more than 128 symbols")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(max = 128, message = "Password can't be more than 128 symbols")
    private String password;

    private Set<Long> rolesId;

    private List<Long> createdTasksId;

    private List<Long> executableTasksId;

    private List<Comment> commentsId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
