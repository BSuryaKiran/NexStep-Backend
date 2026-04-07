package com.placement.api.controller;

import com.placement.api.dto.ApplicationDTO;
import com.placement.api.dto.ApplicationRequest;
import com.placement.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<ApplicationDTO> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationRequest request) {
        ApplicationDTO application = applicationService.createApplication(
                request.getJobId(), 
                request.getUserId(), 
                request.getResumeUrl()
        );
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByUser(@PathVariable Long userId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByUser(userId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByJob(@PathVariable Long jobId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByRecruiter(@PathVariable Long recruiterId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByRecruiter(recruiterId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDTO> getApplication(@PathVariable Long applicationId) {
        ApplicationDTO application = applicationService.getApplication(applicationId);
        return ResponseEntity.ok(application);
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        ApplicationDTO application = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}
