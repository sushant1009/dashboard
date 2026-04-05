package com.finance.dashboard.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DashboardSummaryDTO {
    private double totalIncome;
    private double totalExpense;
    private double balance;
}