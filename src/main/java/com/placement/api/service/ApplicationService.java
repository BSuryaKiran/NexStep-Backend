package com.placement.api.service;

import com.placement.api.dto.ApplicationDTO;
import com.placement.api.entity.Application;
import com.placement.api.entity.ApplicationStatus;
import com.placement.api.entity.Job;
import com.placement.api.entity.User;
import com.placement.api.exception.BadRequestException;
import com.placement.api.exception.ResourceNotFoundException;
import com.placement.api.repository.ApplicationRepository;
import com.placement.api.repository.JobRepository;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public ApplicationDTO createApplication(Long jobId, Long userId, String resumeUrl) {
        if (jobId == null || userId == null) {
            throw new BadRequestException("Job ID and User ID are required");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if already applied
        List<Application> existing = applicationRepository.findByApplicant_Id(userId);
        if (existing != null) {
            for (Application app : existing) {
                // Defensive check to prevent NPE if dirty data exists in DB
                if (app != null && app.getJob() != null && app.getJob().getId() != null) {
                    if (app.getJob().getId().equals(jobId)) {
                        throw new BadRequestException("Already applied for this job");
                    }
                }
            }
        }

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(user);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedDate(new Date()); 
        application.setResume(resumeUrl);

        application = applicationRepository.save(application);

        // Send Email Notification on Application
        try {
            String companyName = job.getPostedBy() != null && job.getPostedBy().getCompanyName() != null 
                ? job.getPostedBy().getCompanyName() : job.getCompany();
            String recruiterName = job.getPostedBy() != null ? job.getPostedBy().getFullName() : "Recruiter";
            
            emailService.sendApplicationReceivedEmail(user.getEmail(), user.getFullName(), job.getTitle(), companyName, recruiterName);
        } catch (Exception e) {
            System.err.println("Could not send application email: " + e.getMessage());
        }

        return convertToDTO(application);
    }

    public List<ApplicationDTO> getApplicationsByUser(Long userId) {
        if (userId == null) return java.util.Collections.emptyList();
        List<Application> apps = applicationRepository.findByApplicant_Id(userId);
        if (apps == null) return java.util.Collections.emptyList();
        return apps.stream()
                .filter(a -> a != null && a.getJob() != null && a.getApplicant() != null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .filter(a -> a != null && a.getJob() != null && a.getApplicant() != null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getApplicationsByJob(Long jobId) {
        if (jobId == null) return java.util.Collections.emptyList();
        List<Application> apps = applicationRepository.findByJob_Id(jobId);
        if (apps == null) return java.util.Collections.emptyList();
        return apps.stream()
                .filter(a -> a != null && a.getJob() != null && a.getApplicant() != null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getApplicationsByRecruiter(Long userId) {
        if (userId == null) return java.util.Collections.emptyList();
        List<Application> apps = applicationRepository.findByJob_PostedBy_Id(userId);
        if (apps == null) return java.util.Collections.emptyList();
        return apps.stream()
                .filter(a -> a != null && a.getJob() != null && a.getApplicant() != null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ApplicationDTO updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        try {
            String statusEnum = status;
            if (status.equalsIgnoreCase("Under Review")) statusEnum = "UNDER_REVIEW";
            if (status.equalsIgnoreCase("Interview Scheduled")) statusEnum = "INTERVIEW_SCHEDULED";
            if (status.equalsIgnoreCase("Pending")) statusEnum = "PENDING";
            
            application.setStatus(ApplicationStatus.valueOf(statusEnum.replace(" ", "_").toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }

        application = applicationRepository.save(application);

        // Debug Log: Let's see what status we just saved
        System.out.println("DEBUG: Application status updated to: " + application.getStatus());

        // Send Email Notification
        if (application.getApplicant() != null && application.getJob() != null) {
            String studentEmail = application.getApplicant().getEmail();
            String studentName = application.getApplicant().getFullName();
            String jobTitle = application.getJob().getTitle();
            ApplicationStatus currentStatus = application.getStatus();

            // Get dynamic company and recruiter names
            String companyName = application.getJob().getPostedBy() != null && application.getJob().getPostedBy().getCompanyName() != null 
                ? application.getJob().getPostedBy().getCompanyName() : application.getJob().getCompany();
            String recruiterName = application.getJob().getPostedBy() != null ? application.getJob().getPostedBy().getFullName() : "Recruiter";

            // Trigger Accepted email for both ACCEPTED and INTERVIEW_SCHEDULED
            if (currentStatus == ApplicationStatus.ACCEPTED || currentStatus == ApplicationStatus.INTERVIEW_SCHEDULED) {
                System.out.println("Processing ACCEPTANCE email (Status: " + currentStatus + ") for: " + studentEmail);
                emailService.sendAcceptanceEmail(studentEmail, studentName, jobTitle, companyName, recruiterName);
            } else if (currentStatus == ApplicationStatus.REJECTED) {
                System.out.println("Processing REJECTED email for: " + studentEmail);
                emailService.sendRejectionEmail(studentEmail, studentName, jobTitle, companyName, recruiterName);
            } else {
                System.out.println("DEBUG: No email triggered for status: " + currentStatus);
            }
        }

        return convertToDTO(application);
    }

    public ApplicationDTO getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return convertToDTO(application);
    }

    public void deleteApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application not found");
        }
        applicationRepository.deleteById(applicationId);
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        
        // Final guard for conversion
        if (application.getJob() != null) {
            dto.setJobId(application.getJob().getId());
            dto.setJobTitle(application.getJob().getTitle());
            dto.setCompanyName(application.getJob().getCompany());
        } else {
            dto.setJobTitle("Deleted Job");
            dto.setCompanyName("Unknown Company");
        }
        
        if (application.getApplicant() != null) {
            dto.setUserId(application.getApplicant().getId());
            dto.setStudentName(application.getApplicant().getFullName());
            dto.setStudentEmail(application.getApplicant().getEmail());
        } else {
            dto.setStudentName("Unknown Student");
        }
        
        // Handle status
        if (application.getStatus() == null) {
            dto.setStatus("Under Review");
        } else {
            String rawStatus = application.getStatus().toString();
            if (rawStatus.equals("PENDING") || rawStatus.equals("UNDER_REVIEW")) {
                dto.setStatus("Under Review");
            } else {
                dto.setStatus(formatStatus(rawStatus.replace("_", " ")));
            }
        }
        
        // Handle date
        if (application.getAppliedDate() != null) {
            dto.setAppliedDate(new SimpleDateFormat("yyyy-MM-dd").format(application.getAppliedDate()));
        } else {
            dto.setAppliedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        
        dto.setResumeUrl(application.getResume());
        return dto;
    }

    private String formatStatus(String status) {
        if (status == null || status.isEmpty()) return "Under Review";
        String[] words = status.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
