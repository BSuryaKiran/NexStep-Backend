package com.placement.api.dto;

public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private Double salary;
    private String postedBy;
    private Boolean active;

    public JobDTO() {}

    public JobDTO(Long id, String title, String description, String company, String location, Double salary, String postedBy, Boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.postedBy = postedBy;
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

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
