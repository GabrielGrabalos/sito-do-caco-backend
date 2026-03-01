package com.caco.sitedocaco.controller.admin;

import com.caco.sitedocaco.dto.request.CreateNewsDTO;
import com.caco.sitedocaco.dto.request.UpdateNewsDTO;
import com.caco.sitedocaco.dto.response.NewsDetailDTO;
import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.security.ratelimit.RateLimit;
import com.caco.sitedocaco.service.NewsService;
import com.caco.sitedocaco.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RateLimit(capacity = 30, refillTokens = 30)
public class NewsAdminController {

    private final NewsService newsService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<NewsSummaryDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("publishDate").descending());
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<NewsDetailDTO> getNewsBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(newsService.getNewsBySlug(slug));
    }

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
        return ResponseEntity.ok(newsService.updateNews(id, dto, null, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable UUID id) {
        newsService.deleteNews(id, null, true);
        return ResponseEntity.noContent().build();
    }
}

