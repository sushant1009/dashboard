package com.finance.dashboard.dto;

import com.finance.dashboard.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTransactionRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotBlank(message = "Category required")
    private String category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 255, message = "Note too long")
    private String note;

    @NotNull(message = "UserId is required")
    private Long userId;
}