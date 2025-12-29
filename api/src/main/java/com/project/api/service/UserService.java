package com.project.api.service;

import com.project.api.entity.User;
import com.project.api.exception.ConflictException;
import com.project.api.exception.InvalidException;
import com.project.api.exception.ResourceNotFoundException;
import com.project.api.repository.UserRepository;
import com.project.api.security.JwtUtils;
import com.project.api.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    @Transactional
    public String verifyUser(String token, String userCode) {
        // Decode JWT token
        Claims claims = jwtUtils.extractAllClaims(token);

        // Extract token type:
        String tokenType = claims.get("type", String.class);

        // Check if token type is verification
        if (!tokenType.equals("email_verification")) {
            throw new InvalidException("Invalid token type");
        }

        // Extract user entity
        User user = claims.get("user", User.class);

        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("User already exists");
        }

        // Extract expected code from token:
        String expectedCode = claims.get("code", String.class);

        // Check of code matches
        if (!passwordEncoder.matches(userCode.toUpperCase(), expectedCode)) {
            throw new InvalidException("Invalid verification code");
        }

        // Generate token for finalizing registration
        return jwtUtils.generateCustomToken(
                Map.of(
                        "type", "finalize_registration",
                        "user", user
                ),
                new UserDetailsImpl(user),
                60 * 60 * 1000 // 1 hour
        );
    }

    /**
     *  Creates a token for user registration and sends a verification email
     *  with a verification code.
     * <p>
     *  User should proceed by sending correct code and token to the verify
     *  email endpoint to complete registration.
     */
    public String beginUserRegistration(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("User already exists");
        }

        // Generate verification code 6 non-numeric characters:
        String code =
                UUID.randomUUID()
                        .toString()
                        .replaceAll("[^a-zA-Z]", "").substring(0, 6)
                        .toUpperCase();

        String hashedCode = passwordEncoder.encode(code);

        // generate token with verification code:
        String token = jwtUtils.generateCustomToken(
                Map.of(
                        "type", "email_verification",
                        "code", hashedCode,
                        "user", user
                ),
                new UserDetailsImpl(user),
                60 * 60 * 1000 // 1 hour
        );

        emailService.sendVerificationEmail(user, code);

        return token;
    }

    /**
     *  Finalizes user registration by updating the user entity with the
     *  provided username.
     *  This method should be called after the user has verified their email
     *  and provided a username.
     * */
    public void finalizeRegistration(String token, String username) {
        // Decode JWT token
        Claims claims = jwtUtils.extractAllClaims(token);

        // Extract token type:
        String tokenType = claims.get("type", String.class);

        // Check if token type is verification
        if (!tokenType.equals("finalize_registration")) {
            throw new InvalidException("Invalid token type");
        }

        // Extract user entity
        User user = claims.get("user", User.class);
        user.setUsername(username);

        userRepository.save(user);
    }

    public String resendVerification(String token) {
        // Decode JWT token
        Claims claims = jwtUtils.extractAllClaims(token);

        // Extract token type:
        String tokenType = claims.get("type", String.class);

        // Check if token type is verification
        if (!tokenType.equals("email_verification")) {
            throw new InvalidException("Invalid token type");
        }

        // Extract user entity
        User user = claims.get("user", User.class);

        return beginUserRegistration(user);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setResetPasswordToken(UUID.randomUUID());

        userRepository.save(user);
        emailService.sendPasswordResetEmail(user);
    }

    @Transactional
    public void completePasswordReset(UUID token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new InvalidException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}