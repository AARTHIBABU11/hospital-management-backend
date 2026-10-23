package com.hospital.dto;

import com.hospital.entity.Role;

public class AuthResponse {
    private String token;
    private Long id;
    private String email;
    private String name;
    private Role role;
    private Long roleId; // doctorId or patientId if applicable

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String email, String name, Role role, Long roleId) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.roleId = roleId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
