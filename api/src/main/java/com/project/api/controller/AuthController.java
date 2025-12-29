package com.project.api.controller;

import com.project.api.annotation.RateLimiter;
import com.project.api.dto.request.*;
import com.project.api.dto.response.JwtResponse;
import com.project.api.entity.User;
import com.project.api.security.UserDetailsImpl;
import com.project.api.security.JwtUtils;
import com.project.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RateLimiter
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest request
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        String token = userService.beginUserRegistration(user);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        String token = userService.verifyUser(request.getToken(), request.getCode());
        return ResponseEntity.ok(token);
    }

    @RateLimiter(requests = 1, time = 1)
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String token) {
        String newToken = userService.resendVerification(token);
        return ResponseEntity.ok(newToken);
    }

    @PostMapping("/finalize-registration")
    public ResponseEntity<?> finalizeRegistration(@Valid @RequestBody FinalizeRegistrationRequest request) {
        userService.finalizeRegistration(request.getToken(), request.getUsername());
        return ResponseEntity.ok("Registration completed successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset instructions sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam UUID token,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        userService.completePasswordReset(token, request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}