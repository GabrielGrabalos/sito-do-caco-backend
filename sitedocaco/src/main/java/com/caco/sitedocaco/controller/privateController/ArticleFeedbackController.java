package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.manual.CreateArticleFeedbackDTO;
import com.caco.sitedocaco.dto.response.manual.ArticleFeedbackDTO;
import com.caco.sitedocaco.security.ratelimit.RateLimit;
import com.caco.sitedocaco.service.ArticleFeedbackService;
import com.caco.sitedocaco.service.ManualArticleService;
import com.caco.sitedocaco.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/article-feedback")
@RequiredArgsConstructor
public class ArticleFeedbackController {
    private final ManualArticleService articleService;
    private final ArticleFeedbackService feedbackService;
    private final UserService userService;

    // ==================== FEEDBACK (apenas criação) ====================

    // Feedback: 10 envios por minuto por usuário é mais que suficiente
    @RateLimit(capacity = 10, refillTokens = 10)
    @PostMapping("/articles/{articleId}/feedback")
    public ResponseEntity<ArticleFeedbackDTO> createFeedback(
            @PathVariable UUID articleId,
            @RequestBody @Valid CreateArticleFeedbackDTO dto) {

        var feedback = feedbackService.createFeedback(articleId, dto, userService.getCurrentUser());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleFeedbackDTO.fromEntity(feedback));
    }
}
