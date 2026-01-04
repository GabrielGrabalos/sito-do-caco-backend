package com.caco.sitedocaco.entity.manual;

import com.caco.sitedocaco.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class ArticleFeedback {
    /*
    4. **`ArticleFeedback`**
        - `UUID id`
        - `ManualArticle article` (ManyToOne)
        - `User user` (ManyToOne, Nullable)
        - `Boolean isHelpful`
        - `String comment` (Nullable)
        - `LocalDateTime postedAt`
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private ManualArticle article;

    @ManyToOne
    private User user;

    private Boolean isHelpful;

    private String comment;

    private LocalDateTime postedAt;
}
