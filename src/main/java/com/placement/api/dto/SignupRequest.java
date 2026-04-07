package com.placement.api.dto;

public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String role; // STUDENT, EMPLOYER, PLACEMENT_OFFICER, ADMIN
    private String phone;
    private String companyName; // optional, for employers

    public SignupRequest() {}

    public SignupRequest(String email, String password, String fullName, String role, String phone, String companyName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.companyName = companyName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
