package com.caco.sitedocaco.controller.privateController;

import com.caco.sitedocaco.dto.request.CreateNewsDTO;
import com.caco.sitedocaco.dto.request.UpdateNewsDTO;
import com.caco.sitedocaco.dto.response.NewsDetailDTO;
import com.caco.sitedocaco.security.ratelimit.RateLimit;
import com.caco.sitedocaco.service.NewsService;
import com.caco.sitedocaco.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/editor/news")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EDITOR')")
@RateLimit(capacity = 20, refillTokens = 20)
public class NewsEditorController {

    private final NewsService newsService;
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDetailDTO> createNews(
            @ModelAttribute @Valid CreateNewsDTO dto) throws IOException {
        UUID userId = userService.getCurrentUser().getId();
        NewsDetailDTO created = newsService.createNews(dto, userId);
        return ResponseEntity.created(URI.create("/api/public/news/" + created.slug())).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDetailDTO> updateNews(
            @PathVariable UUID id,
            @ModelAttribute UpdateNewsDTO dto) throws IOException {
        UUID userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(newsService.updateNews(id, dto, userId, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable UUID id) {
        UUID userId = userService.getCurrentUser().getId();
        newsService.deleteNews(id, userId, false);
        return ResponseEntity.noContent().build();
    }
}

