package com.placement.api.config;

import com.placement.api.entity.User;
import com.placement.api.entity.UserRole;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create Demo Student
        createDemoUser("student@demo.com", "student123", "Demo Student", UserRole.STUDENT);
        
        // Create Demo Recruiter
        createDemoUser("recruiter@demo.com", "recruiter123", "Demo Recruiter", UserRole.EMPLOYER);
        
        // Create Demo Placement Officer
        createDemoUser("officer@demo.com", "officer123", "Demo Officer", UserRole.PLACEMENT_OFFICER);
        
        // Create Demo Admin
        createDemoUser("admin@demo.com", "admin123", "Demo Admin", UserRole.ADMIN);
    }

    private void createDemoUser(String email, String password, String fullName, UserRole role) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setRole(role);
            user.setActive(true);
            userRepository.save(user);
            System.out.println("Created demo " + role + " account: " + email);
        }
    }
}
