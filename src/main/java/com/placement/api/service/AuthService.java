package com.placement.api.service;

import com.placement.api.config.JwtTokenProvider;
import com.placement.api.dto.AuthResponse;
import com.placement.api.dto.LoginRequest;
import com.placement.api.dto.SignupRequest;
import com.placement.api.dto.UserDTO;
import com.placement.api.entity.User;
import com.placement.api.entity.UserRole;
import com.placement.api.exception.BadRequestException;
import com.placement.api.exception.UnauthorizedException;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.getActive()) {
            throw new UnauthorizedException("Account is inactive");
        }

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().toString(),
                user.getActive()
        );

        return new AuthResponse(
                true,
                "Login successful",
                token,
                refreshToken,
                userDTO,
                null
        );
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        if (signupRequest.getPassword() == null || signupRequest.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setPhone(signupRequest.getPhone());
        user.setActive(true);
        user.setEmailVerified(false);

        if (signupRequest.getRole() != null) {
            try {
                user.setRole(UserRole.valueOf(signupRequest.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role");
            }
        } else {
            user.setRole(UserRole.STUDENT);
        }

        if (user.getRole() == UserRole.EMPLOYER) {
            user.setCompanyName(signupRequest.getCompanyName());
        }

        user = userRepository.save(user);

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().toString(),
                user.getActive()
        );

        return new AuthResponse(
                true,
                "Signup successful",
                token,
                refreshToken,
                userDTO,
                null
        );
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String newToken = tokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        String newRefreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().toString(),
                user.getActive()
        );

        return new AuthResponse(
                true,
                "Token refreshed successfully",
                newToken,
                newRefreshToken,
                userDTO,
                null
        );
    }
}
