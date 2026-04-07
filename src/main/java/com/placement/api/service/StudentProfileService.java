package com.placement.api.service;

import com.placement.api.dto.StudentProfileDTO;
import com.placement.api.entity.StudentProfile;
import com.placement.api.entity.User;
import com.placement.api.exception.ResourceNotFoundException;
import com.placement.api.repository.StudentProfileRepository;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public StudentProfileDTO getStudentProfile(Long userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        return convertToDTO(profile);
    }

    public StudentProfileDTO createOrUpdateStudentProfile(Long userId, StudentProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<StudentProfile> existingProfile = studentProfileRepository.findByUserId(userId);
        StudentProfile profile;

        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
        } else {
            profile = new StudentProfile();
            profile.setUser(user);
        }

        if (profileDTO.getPhone() != null) {
            profile.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getUniversity() != null) {
            profile.setUniversity(profileDTO.getUniversity());
        }
        if (profileDTO.getDepartment() != null) {
            profile.setDepartment(profileDTO.getDepartment());
        }
        if (profileDTO.getPassoutYear() != null) {
            profile.setPassoutYear(profileDTO.getPassoutYear());
        }
        if (profileDTO.getCgpa() != null) {
            profile.setCgpa(profileDTO.getCgpa());
        }
        if (profileDTO.getSkills() != null) {
            profile.setSkills(profileDTO.getSkills());
        }
        if (profileDTO.getBio() != null) {
            profile.setBio(profileDTO.getBio());
        }
        if (profileDTO.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(profileDTO.getProfilePictureUrl());
        }
        if (profileDTO.getResumeUrl() != null) {
            profile.setResumeUrl(profileDTO.getResumeUrl());
        }

        profile = studentProfileRepository.save(profile);
        return convertToDTO(profile);
    }

    private StudentProfileDTO convertToDTO(StudentProfile profile) {
        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setPhone(profile.getPhone());
        dto.setUniversity(profile.getUniversity());
        dto.setDepartment(profile.getDepartment());
        dto.setPassoutYear(profile.getPassoutYear());
        dto.setCgpa(profile.getCgpa());
        dto.setSkills(profile.getSkills());
        dto.setBio(profile.getBio());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        dto.setResumeUrl(profile.getResumeUrl());
        return dto;
    }
}
