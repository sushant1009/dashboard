package com.finance.dashboard.service;

import com.finance.dashboard.dto.CreateUserRequest;
import com.finance.dashboard.dto.UserDTO;
import com.finance.dashboard.entity.Status;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //new user registration
    public UserDTO createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        return toDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //Build userDTO from user object
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(Role.valueOf(user.getRole().name()));
        dto.setStatus(Status.valueOf(user.getStatus().name()));
        return dto;
    }

    public Optional<User> getUserById(Long id){

        return userRepository.findById(id);
    }

    //Update user role
    public UserDTO changeUserRole(Role role, Long id) throws BadRequestException {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        if (user.getRole() == role) {
            throw new BadRequestException("User already has this role");
        }
        user.setRole(role);
        user = userRepository.save(user);

        return toDTO(user);
    }

    //Update user status
    public UserDTO changeUserStatus(Status status, Long id) throws BadRequestException {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        if (user.getStatus() == status) {
            throw new BadRequestException("User already has this Status");
        }
        user.setStatus(status);
        user = userRepository.save(user);

        return toDTO(user);
    }

    public Optional<User> findByEmail(@Email(message = "Invalid email") @NotBlank(message = "Email required") String email) {
       return userRepository.findByEmail(email);
    }
}