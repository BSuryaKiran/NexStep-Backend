package com.placement.api.controller;

import com.placement.api.dto.JobDTO;
import com.placement.api.entity.Job;
import com.placement.api.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody Job job, @RequestParam(required = false) Long userId) {
        // Fallback for demo/testing until full auth is wired up
        Long effectiveUserId = (userId != null) ? userId : 2L; // Default to demo recruiter if not provided
        JobDTO newJob = jobService.createJob(job, effectiveUserId);
        return new ResponseEntity<>(newJob, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllActiveJobs() {
        // Return DTOs instead of entities to avoid serialization issues
        List<JobDTO> jobs = jobService.getAllActiveJobs().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/recruiter/{userId}")
    public ResponseEntity<List<JobDTO>> getJobsByRecruiter(@PathVariable Long userId) {
        List<JobDTO> jobs = jobService.getJobsByRecruiter(userId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{company}")
    public ResponseEntity<List<Job>> getJobsByCompany(@PathVariable String company) {
        List<Job> jobs = jobService.getJobsByCompany(company);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        JobDTO updatedJob = jobService.updateJob(id, jobDetails);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    private JobDTO convertToDTO(Job job) {
        return new JobDTO(
            job.getId(),
            job.getTitle(),
            job.getDescription(),
            job.getCompany(),
            job.getLocation(),
            job.getSalary(),
            job.getPostedBy() != null ? job.getPostedBy().getFullName() : "Unknown",
            job.getActive()
        );
    }
}
