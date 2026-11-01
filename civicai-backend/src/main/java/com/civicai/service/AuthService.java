package com.civicai.service;

import com.civicai.config.JwtUtils;
import com.civicai.dto.AuthDTOs.*;
import com.civicai.entity.Department;
import com.civicai.entity.User;
import com.civicai.enums.Role;
import com.civicai.repository.DepartmentRepository;
import com.civicai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmailOrMobile(req.getIdentifier(), req.getIdentifier())
                .orElseThrow(() -> new RuntimeException("Invalid credentials or user not found."));

        if ("BLOCKED".equalsIgnoreCase(user.getAccountStatus())) {
            throw new RuntimeException("Account has been blocked by system administration.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getIdentifier(), req.getPassword())
        );

        String token = jwtUtils.generateJwtToken(user.getEmail() != null ? user.getEmail() : user.getMobile(), user.getRole().name());

        Long deptId = user.getDepartment() != null ? user.getDepartment().getId() : null;
        String deptName = user.getDepartment() != null ? user.getDepartment().getName() : null;

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getMobile(), user.getRole().name(), deptId, deptName);
    }

    public AuthResponse register(RegisterRequest req) {
        if (req.getEmail() != null && !req.getEmail().trim().isEmpty() && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }
        if (req.getMobile() != null && !req.getMobile().trim().isEmpty() && userRepository.existsByMobile(req.getMobile())) {
            throw new RuntimeException("Mobile number is already registered.");
        }

        Role role = Role.ROLE_CITIZEN;
        if (req.getRole() != null && req.getRole().equalsIgnoreCase("ROLE_DEPARTMENT_ADMIN")) {
            role = Role.ROLE_DEPARTMENT_ADMIN;
        }

        User user = new User(req.getName(), req.getEmail(), req.getMobile(), passwordEncoder.encode(req.getPassword()), role);

        if (req.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(req.getDepartmentId()).orElse(null);
            user.setDepartment(dept);
        }

        userRepository.save(user);

        String token = jwtUtils.generateJwtToken(user.getEmail() != null ? user.getEmail() : user.getMobile(), user.getRole().name());
        Long deptId = user.getDepartment() != null ? user.getDepartment().getId() : null;
        String deptName = user.getDepartment() != null ? user.getDepartment().getName() : null;

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getMobile(), user.getRole().name(), deptId, deptName);
    }
}
