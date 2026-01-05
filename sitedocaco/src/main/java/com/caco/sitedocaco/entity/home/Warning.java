package com.caco.sitedocaco.entity.home;

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
        - `LocalDateTime startsAt`
        - `LocalDateTime expiresAt`
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String markdownText;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
