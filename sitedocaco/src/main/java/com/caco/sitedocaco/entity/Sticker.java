package com.caco.sitedocaco.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table
@Data
public class Sticker {
    /*
    1. **`Sticker`**
        - `UUID id`
        - `String name`
        - `String description`
        - `String imageUrl`
        - `Event originEvent` (ManyToOne, Nullable)
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 600)
    private String description;

    private String imageUrl;

    private Event originEvent;
}
