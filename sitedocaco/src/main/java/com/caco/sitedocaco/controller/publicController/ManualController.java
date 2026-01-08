package com.caco.sitedocaco.controller.publicController;

import com.caco.sitedocaco.dto.request.manual.CreateArticleFeedbackDTO;
import com.caco.sitedocaco.dto.response.manual.ArticleFeedbackDTO;
import com.caco.sitedocaco.dto.response.manual.ManualArticleDTO;
import com.caco.sitedocaco.dto.response.manual.ManualCategoryDTO;
import com.caco.sitedocaco.dto.response.manual.ManualChapterDTO;
import com.caco.sitedocaco.service.ArticleFeedbackService;
import com.caco.sitedocaco.service.ManualArticleService;
import com.caco.sitedocaco.service.ManualCategoryService;
import com.caco.sitedocaco.service.ManualChapterService;
import com.caco.sitedocaco.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/manual")
@RequiredArgsConstructor
public class ManualController {

    private final ManualCategoryService categoryService;
    private final ManualChapterService chapterService;
    private final ManualArticleService articleService;
    private final ArticleFeedbackService feedbackService;
    private final UserService userService;

    // ==================== CATEGORIAS ====================

    @GetMapping("/categories")
    public ResponseEntity<List<ManualCategoryDTO>> getAllCategories() {
        List<ManualCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{slug}")
    public ResponseEntity<ManualCategoryDTO> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ManualCategoryDTO.fromEntity(
                categoryService.getCategoryBySlug(slug),
                null
        ));
    }

    // ==================== CAPÍTULOS ====================

    @GetMapping("/chapters")
    public ResponseEntity<List<ManualChapterDTO>> getAllChapters() {
        List<ManualChapterDTO> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/chapters/category/{categoryId}")
    public ResponseEntity<List<ManualChapterDTO>> getChaptersByCategory(@PathVariable UUID categoryId) {
        List<ManualChapterDTO> chapters = chapterService.getChaptersByCategory(categoryId);
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/chapters/{slug}")
    public ResponseEntity<ManualChapterDTO> getChapterBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ManualChapterDTO.fromEntity(
                chapterService.getChapterBySlug(slug),
                null
        ));
    }

    // ==================== ARTIGOS ====================

    @GetMapping("/articles")
    public ResponseEntity<List<ManualArticleDTO>> getAllArticles() {
        List<ManualArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/articles/chapter/{chapterId}")
    public ResponseEntity<List<ManualArticleDTO>> getArticlesByChapter(@PathVariable UUID chapterId) {
        List<ManualArticleDTO> articles = articleService.getArticlesByChapter(chapterId);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ManualArticleDTO> getArticleById(@PathVariable UUID id) {
        ManualArticleDTO article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/articles/slug/{slug}")
    public ResponseEntity<ManualArticleDTO> getArticleBySlug(@PathVariable String slug) {
        ManualArticleDTO article = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(article);
    }
}