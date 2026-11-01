package com.civicai.controller;

import com.civicai.config.UserDetailsImpl;
import com.civicai.dto.ComplaintDTOs.*;
import com.civicai.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/complaints/{id}/flag")
    @PreAuthorize("hasRole('DEPARTMENT_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> flagComplaint(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody FlagRequestDTO dto) {
        adminService.flagComplaint(id, userDetails.getUser().getId(), dto);
        return ResponseEntity.ok(Map.of("message", "Complaint flagged successfully"));
    }

    @GetMapping("/users/{id}/risk")
    @PreAuthorize("hasRole('DEPARTMENT_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserReliability(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserReliability(id));
    }

    @PostMapping("/users/{id}/block")
    @PreAuthorize("hasRole('DEPARTMENT_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> blockUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody BlockUserRequestDTO dto) {
        adminService.blockUser(id, userDetails.getUser().getId(), dto.getReason());
        return ResponseEntity.ok(Map.of("message", "User blocked successfully"));
    }

    @PostMapping("/users/{id}/unblock")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> unblockUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        adminService.unblockUser(id, userDetails.getUser().getId());
        return ResponseEntity.ok(Map.of("message", "User unblocked successfully"));
    }
}
