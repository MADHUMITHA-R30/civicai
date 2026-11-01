package com.civicai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "issue_categories")
public class IssueCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department defaultDepartment;

    @Column(nullable = false)
    private Boolean active = true;

    public IssueCategory() {}

    public IssueCategory(String name, String description, Department defaultDepartment) {
        this.name = name;
        this.description = description;
        this.defaultDepartment = defaultDepartment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Department getDefaultDepartment() { return defaultDepartment; }
    public void setDefaultDepartment(Department defaultDepartment) { this.defaultDepartment = defaultDepartment; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
