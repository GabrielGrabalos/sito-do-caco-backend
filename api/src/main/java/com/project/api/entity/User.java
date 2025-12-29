package com.project.api.entity;

import com.project.api.annotation.Unique;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Unique(message = "Username is already taken")
    @Column(nullable = false, unique = true)
    private String username;

    @Unique(message = "Email already registered")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private UUID resetPasswordToken;
}