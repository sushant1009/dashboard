package com.finance.dashboard.controller;

import com.finance.dashboard.dto.*;
import com.finance.dashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Endpoint to retrieve all users data
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Change user role
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/change-role")
    public ResponseEntity<?> changeUserRole(@Valid @RequestBody ChangeRoleRequest changeRoleRequest) throws BadRequestException {
        return ResponseEntity.ok(userService.changeUserRole(changeRoleRequest.getRole(), changeRoleRequest.getUserId()));
    }

    //Change user status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/change-status")
    public ResponseEntity<?> changeUserStatus(@Valid @RequestBody ChangeStatusRequest changeStatusRequest) throws BadRequestException {
        return ResponseEntity.ok(userService.changeUserStatus(changeStatusRequest.getStatus(), changeStatusRequest.getUserId()));
    }



}