package com.civicai.controller;

import com.civicai.dto.AnalyticsDTOs.DepartmentAnalyticsDTO;
import com.civicai.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/department")
    public ResponseEntity<DepartmentAnalyticsDTO> getDepartmentAnalytics(@RequestParam(required = false) Long deptId) {
        return ResponseEntity.ok(analyticsService.getDepartmentAnalytics(deptId));
    }
}
