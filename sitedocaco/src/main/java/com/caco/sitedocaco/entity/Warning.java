package com.caco.sitedocaco.entity;

import jakarta.persistence.*;
import lombok.Data;

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
}
