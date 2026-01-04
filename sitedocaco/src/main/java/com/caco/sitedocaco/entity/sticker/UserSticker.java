package com.caco.sitedocaco.entity.sticker;

import com.caco.sitedocaco.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table
public class UserSticker {
    /*
    1. **`UserSticker`**
        - `UUID id`
        - `User user` (ManyToOne)
        - `Sticker sticker` (ManyToOne)
        - `LocalDateTime obtainedAt`
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Sticker sticker;

    private LocalDateTime obtainedAt;
}
