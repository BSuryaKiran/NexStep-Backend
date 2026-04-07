package com.placement.api.dto;

public class ApplicationDTO {
    private Long id;
    private Long jobId;
    private Long userId;
    private String status;
    private String appliedDate;
    private String resumeUrl;
    private String jobTitle;
    private String companyName;
    private String studentName;
    private String studentEmail;

    public ApplicationDTO() {}

    public ApplicationDTO(Long id, Long jobId, Long userId, String status, String appliedDate, String resumeUrl, String jobTitle, String companyName, String studentName, String studentEmail) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.status = status;
        this.appliedDate = appliedDate;
        this.resumeUrl = resumeUrl;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
}
