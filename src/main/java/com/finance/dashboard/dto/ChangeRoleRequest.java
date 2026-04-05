package com.finance.dashboard.dto;

import com.finance.dashboard.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleRequest {
    @NotNull(message = "UserId required")
    private Long userId;

    @NotNull(message = "Role required")
    private Role role;
}
