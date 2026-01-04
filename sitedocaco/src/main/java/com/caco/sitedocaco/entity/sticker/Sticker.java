package com.caco.sitedocaco.entity.sticker;

import com.caco.sitedocaco.entity.event.Event;
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
        - `Event originEvent` (OneToOne Nullable)
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 600)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    private Event originEvent;
}
