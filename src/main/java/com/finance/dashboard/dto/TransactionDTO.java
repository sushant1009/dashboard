package com.finance.dashboard.dto;

import com.finance.dashboard.entity.TransactionType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private Double amount;
    private TransactionType type;
    private String category;
    private LocalDate date;
    private String note;
    private Long userId;
}