package com.caco.sitedocaco.service;

import com.caco.sitedocaco.dto.request.CreateNewsDTO;
import com.caco.sitedocaco.dto.request.UpdateNewsDTO;
import com.caco.sitedocaco.dto.response.NewsDetailDTO;
import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.enums.ImageType;
import com.caco.sitedocaco.entity.home.News;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.NewsRepository;
import com.caco.sitedocaco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ImgBBService imgBBService;

    @Transactional(readOnly = true)
    public List<NewsSummaryDTO> getLatestNews(int limit) {
        return newsRepository.findAllSummaries(PageRequest.of(0, limit)).getContent();
    }

    @Transactional(readOnly = true)
    public Page<NewsSummaryDTO> getAllNews(Pageable pageable) {
        return newsRepository.findAllSummaries(pageable);
    }

    @Transactional(readOnly = true)
    public NewsDetailDTO getNewsBySlug(String slug) {
        return newsRepository.findDetailBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Notícia não encontrada: " + slug));
    }

    @Transactional
    public NewsDetailDTO createNews(CreateNewsDTO dto, UUID authorId) throws IOException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

        // Valida unicidade do slug
        if (newsRepository.existsBySlug(dto.slug())) {
            throw new IllegalArgumentException("Já existe uma notícia com este slug: " + dto.slug());
        }

        News news = new News();
        news.setTitle(dto.title());
        news.setSlug(dto.slug());
        news.setSummary(dto.summary());
        news.setContent(dto.content());
        news.setAuthor(author);
        news.setPublishDate(LocalDateTime.now());

        if (dto.coverImage() != null && !dto.coverImage().isEmpty()) {
            String url = imgBBService.uploadImage(dto.coverImage(), ImageType.NEWS_COVER);
            news.setCoverImage(url);
        }

        return toDetailDTO(newsRepository.save(news));
    }

    @Transactional
    public NewsDetailDTO updateNews(UUID id, UpdateNewsDTO dto, UUID requesterId, boolean isAdmin) throws IOException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notícia não encontrada"));

        validateOwnership(news, requesterId, isAdmin);

        if (dto.title() != null && !dto.title().isBlank()) {
            news.setTitle(dto.title());
        }

        if (dto.slug() != null && !dto.slug().isBlank()) {
            // Se o slug está sendo alterado, valida unicidade
            if (!dto.slug().equals(news.getSlug()) && newsRepository.existsBySlug(dto.slug())) {
                throw new IllegalArgumentException("Já existe uma notícia com este slug: " + dto.slug());
            }
            news.setSlug(dto.slug());
        }

        if (dto.summary() != null) news.setSummary(dto.summary());
        if (dto.content() != null) news.setContent(dto.content());

        // Remoção explícita da imagem
        if (Boolean.TRUE.equals(dto.removeCoverImage())) {
            news.setCoverImage(null);
        }
        // Upload de nova imagem (sobrescreve remoção se ambos vierem, nova imagem tem prioridade)
        if (dto.coverImage() != null && !dto.coverImage().isEmpty()) {
            String url = imgBBService.uploadImage(dto.coverImage(), ImageType.NEWS_COVER);
            news.setCoverImage(url);
        }

        return toDetailDTO(newsRepository.save(news));
    }

    @Transactional
    public void deleteNews(UUID id, UUID requesterId, boolean isAdmin) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notícia não encontrada"));

        validateOwnership(news, requesterId, isAdmin);

        newsRepository.delete(news);
    }

    private void validateOwnership(News news, UUID requesterId, boolean isAdmin) {
        if (isAdmin) return;
        if (!news.getAuthor().getId().equals(requesterId)) {
            throw new AccessDeniedException("Você só pode alterar notícias criadas por você.");
        }
    }

    private NewsDetailDTO toDetailDTO(News news) {
        return new NewsDetailDTO(
                news.getId(),
                news.getTitle(),
                news.getSlug(),
                news.getSummary(),
                news.getContent(),
                news.getCoverImage(),
                news.getPublishDate()
        );
    }
}

