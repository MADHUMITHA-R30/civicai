package com.civicai.controller;

import com.civicai.entity.Department;
import com.civicai.entity.IssueCategory;
import com.civicai.repository.DepartmentRepository;
import com.civicai.repository.IssueCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IssueCategoryRepository categoryRepository;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<IssueCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findByActiveTrue());
    }
}
