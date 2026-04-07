package com.placement.api.controller;

import com.placement.api.dto.StudentProfileDTO;
import com.placement.api.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-profile")
@CrossOrigin(origins = "*")
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(@PathVariable Long userId) {
        StudentProfileDTO profile = studentProfileService.getStudentProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<StudentProfileDTO> createStudentProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfileDTO profileDTO) {
        StudentProfileDTO profile = studentProfileService.createOrUpdateStudentProfile(userId, profileDTO);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<StudentProfileDTO> updateStudentProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfileDTO profileDTO) {
        StudentProfileDTO profile = studentProfileService.createOrUpdateStudentProfile(userId, profileDTO);
        return ResponseEntity.ok(profile);
    }
}
