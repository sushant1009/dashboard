package com.finance.dashboard.dto;

import com.finance.dashboard.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusRequest {
    @NotNull(message = "UserId required")
    private Long userId;

    @NotNull(message = "Status required")
    private Status status;
}
