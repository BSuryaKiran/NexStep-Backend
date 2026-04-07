package com.placement.api.service;

import com.placement.api.dto.JobDTO;
import com.placement.api.entity.Job;
import com.placement.api.entity.User;
import com.placement.api.repository.JobRepository;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public JobDTO createJob(Job job, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        job.setPostedBy(user);
        Job savedJob = jobRepository.save(job);
        return convertToDTO(savedJob);
    }

    public List<Job> getAllActiveJobs() {
        return jobRepository.findByActiveTrue();
    }

    public List<JobDTO> getJobsByRecruiter(Long userId) {
        return jobRepository.findByPostedBy_Id(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<JobDTO> getJobById(Long id) {
        return jobRepository.findById(id).map(this::convertToDTO);
    }

    public List<Job> getJobsByCompany(String company) {
        return jobRepository.findByCompany(company);
    }

    public JobDTO updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setSalary(jobDetails.getSalary());
        job.setLocation(jobDetails.getLocation());
        job.setCompany(jobDetails.getCompany());
        Job updatedJob = jobRepository.save(job);
        return convertToDTO(updatedJob);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
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
