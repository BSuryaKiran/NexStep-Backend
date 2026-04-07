package com.placement.api.controller;

import com.placement.api.dto.AuthResponse;
import com.placement.api.dto.LoginRequest;
import com.placement.api.dto.SignupRequest;
import com.placement.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring("Bearer ".length()) : bearerToken;
        AuthResponse response = authService.refreshToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String bearerToken) {
        // If the request reaches this point, the JWT filter has already validated the token
        return new ResponseEntity<>(
                new AuthResponse(true, "Token is valid", null, null, null, null),
                HttpStatus.OK
        );
    }
}
