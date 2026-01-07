package com.caco.sitedocaco.entity.home;

import com.caco.sitedocaco.entity.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table
public class Warning {
    /*
    2. `Warning` (Avisos)
        - `UUID id`
        - `String markdownText`
        - `SeverityLevel severityLevel` (ENUM: LOW, MEDIUM, HIGH, CRITICAL) [default: MEDIUM]
        - `LocalDateTime startsAt`
        - `LocalDateTime expiresAt`
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String markdownText;

    @Enumerated(EnumType.STRING)
    SeverityLevel severityLevel = SeverityLevel.MEDIUM;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
