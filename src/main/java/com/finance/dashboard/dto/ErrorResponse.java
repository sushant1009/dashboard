package com.finance.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
}