package com.placement.api.dto;

public class StudentProfileDTO {
    private Long id;
    private Long userId;
    private String phone;
    private String university;
    private String department;
    private String passoutYear;
    private String cgpa;
    private String skills;
    private String bio;
    private String profilePictureUrl;
    private String resumeUrl;

    public StudentProfileDTO() {}

    public StudentProfileDTO(Long id, Long userId, String phone, String university, String department, String passoutYear, String cgpa, String skills, String bio, String profilePictureUrl, String resumeUrl) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.university = university;
        this.department = department;
        this.passoutYear = passoutYear;
        this.cgpa = cgpa;
        this.skills = skills;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.resumeUrl = resumeUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPassoutYear() { return passoutYear; }
    public void setPassoutYear(String passoutYear) { this.passoutYear = passoutYear; }

    public String getCgpa() { return cgpa; }
    public void setCgpa(String cgpa) { this.cgpa = cgpa; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}
