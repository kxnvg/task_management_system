package com.kxnvg.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kxnvg.taskmanagement.entity.Comment;
import com.kxnvg.taskmanagement.entity.enums.UserRole;
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

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private UserRole role;

    private List<Long> createdTasksId;

    private List<Long> executableTasksId;

    private List<Comment> commentsId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAT;
}
