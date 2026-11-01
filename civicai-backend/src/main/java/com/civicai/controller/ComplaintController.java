package com.civicai.controller;

import com.civicai.config.UserDetailsImpl;
import com.civicai.dto.ComplaintDTOs.*;
import com.civicai.dto.ResolutionDTOs.*;
import com.civicai.enums.ComplaintStatus;
import com.civicai.enums.PriorityLevel;
import com.civicai.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateComplaintDTO dto) {
        return ResponseEntity.ok(complaintService.createComplaint(userDetails.getUser().getId(), dto));
    }

    @PostMapping("/voice-process")
    public ResponseEntity<Map<String, Object>> processVoice(@RequestBody VoiceProcessRequest req) {
        return ResponseEntity.ok(complaintService.processVoiceInput(req.getAudioBase64()));
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<Map<String, Object>> checkDuplicate(@RequestBody Map<String, Object> payload) {
        Long categoryId = ((Number) payload.get("categoryId")).longValue();
        String description = (String) payload.get("description");
        String address = (String) payload.get("address");
        Double lat = payload.get("latitude") != null ? ((Number) payload.get("latitude")).doubleValue() : null;
        Double lon = payload.get("longitude") != null ? ((Number) payload.get("longitude")).doubleValue() : null;

        return ResponseEntity.ok(complaintService.checkDuplicate(categoryId, description, address, lat, lon));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponseDTO>> getMyComplaints(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(complaintService.getMyComplaints(userDetails.getUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> getComplaintById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @GetMapping("/department")
    public ResponseEntity<List<ComplaintResponseDTO>> getDepartmentComplaints(
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        ComplaintStatus statusEnum = status != null && !status.isEmpty() ? ComplaintStatus.valueOf(status) : null;
        PriorityLevel priorityEnum = priority != null && !priority.isEmpty() ? PriorityLevel.valueOf(priority) : null;
        return ResponseEntity.ok(complaintService.getDepartmentComplaints(deptId, statusEnum, priorityEnum));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponseDTO> updateStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody StatusUpdateDTO dto) {
        return ResponseEntity.ok(complaintService.updateStatus(id, userDetails.getUser().getId(), dto));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<ComplaintResponseDTO> resolveComplaint(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody ResolveComplaintDTO dto) {
        return ResponseEntity.ok(complaintService.resolveComplaint(id, userDetails.getUser().getId(), dto));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<ComplaintResponseDTO> verifyResolution(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody VerificationRequestDTO dto) {
        return ResponseEntity.ok(complaintService.verifyResolution(id, userDetails.getUser().getId(), dto));
    }
}
