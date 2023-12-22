package com.kxnvg.taskmanagement.dto;

import com.kxnvg.taskmanagement.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "First name can't be empty")
    @Size(max = 128, message = "First name can't be more than 128 symbols")
    private String firstname;

    @NotBlank(message = "Last name can't be empty")
    @Size(max = 128, message = "Last name can't be more than 128 symbols")
    private String lastname;

    @NotBlank(message = "Email can't be empty")
    @Size(max = 128, message = "Email can't be more than 128 symbols")
    private String email;

    @NotBlank(message = "Password can't be empty")
    @Size(max = 128, message = "Password can't be more than 128 symbols")
    private String password;

    @NotNull(message = "User role can't be null")
    private UserRole role;
}
