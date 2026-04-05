package com.finance.dashboard.dto;

import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Status status;

}