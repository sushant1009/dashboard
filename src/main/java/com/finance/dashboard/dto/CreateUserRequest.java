package com.finance.dashboard.dto;

import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Status is required")
    private Status status;
}