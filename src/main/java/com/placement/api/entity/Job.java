package com.placement.api.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String company;

    private String location;

    private String salary;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @Column(name = "posted_date")
    private Date postedDate = new Date();

    private Boolean active = true;
    private String type;
    private String deadline;
    private String examDate;
    private String mode;
    @Column(columnDefinition = "TEXT")
    private String interviewDetails;
    private String skills;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Application> applicationsList = new java.util.ArrayList<>();

    public Job() {}

    public Job(Long id, String title, String description, String company, String location, String salary, User postedBy, Date postedDate, Boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.postedBy = postedBy;
        this.postedDate = postedDate;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }

    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getExamDate() { return examDate; }
    public void setExamDate(String examDate) { this.examDate = examDate; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getInterviewDetails() { return interviewDetails; }
    public void setInterviewDetails(String interviewDetails) { this.interviewDetails = interviewDetails; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
}
