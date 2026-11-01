package com.civicai.dto;

public class AuthDTOs {

    public static class LoginRequest {
        private String identifier; // email or mobile
        private String password;

        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String name;
        private String email;
        private String mobile;
        private String password;
        private String role; // ROLE_CITIZEN, ROLE_DEPARTMENT_ADMIN
        private Long departmentId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    }

    public static class AuthResponse {
        private String token;
        private Long id;
        private String name;
        private String email;
        private String mobile;
        private String role;
        private Long departmentId;
        private String departmentName;

        public AuthResponse(String token, Long id, String name, String email, String mobile, String role, Long departmentId, String departmentName) {
            this.token = token;
            this.id = id;
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.role = role;
            this.departmentId = departmentId;
            this.departmentName = departmentName;
        }

        public String getToken() { return token; }
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }
        public String getRole() { return role; }
        public Long getDepartmentId() { return departmentId; }
        public String getDepartmentName() { return departmentName; }
    }
}
