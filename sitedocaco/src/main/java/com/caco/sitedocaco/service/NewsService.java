package com.caco.sitedocaco.service;



import com.caco.sitedocaco.dto.request.CreateNewsDTO;
import com.caco.sitedocaco.dto.request.UpdateNewsDTO;
import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.home.News;
import com.caco.sitedocaco.exception.ResourceNotFoundException;
import com.caco.sitedocaco.repository.NewsRepository;
import com.caco.sitedocaco.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NewsSummaryDTO> getLatestNews(int limit) {
        return newsRepository.findAllSummaries(PageRequest.of(0, limit)).getContent();
    }

    @Transactional
    public News createNews(CreateNewsDTO dto, UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

        News news = new News();
        news.setTitle(dto.title());
        news.setSummary(dto.summary());
        news.setContent(dto.content());
        news.setCoverImage(dto.coverImage());
        news.setAuthor(author);
        news.setPublishDate(LocalDateTime.now());

        // Regra de Negócio: Gerar Slug único [cite: 276]
        news.setSlug(generateUniqueSlug(dto.title()));

        return newsRepository.save(news);
    }

    @Transactional
    public News updateNews(UUID id, UpdateNewsDTO dto, UUID requesterId, boolean isAdmin) throws AccessDeniedException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notícia não encontrada"));

        // REGRA DE NEGÓCIO: Só Admin ou o Próprio Autor podem editar
        validateOwnership(news, requesterId, isAdmin);

        if (dto.title() != null) news.setTitle(dto.title());
        if (dto.summary() != null) news.setSummary(dto.summary());
        if (dto.content() != null) news.setContent(dto.content());
        if (dto.coverImage() != null) news.setCoverImage(dto.coverImage());

        return newsRepository.save(news);
    }

    @Transactional
    public void deleteNews(UUID id, UUID requesterId, boolean isAdmin) throws AccessDeniedException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notícia não encontrada"));

        // REGRA DE NEGÓCIO: Só Admin ou o Próprio Autor podem deletar
        validateOwnership(news, requesterId, isAdmin);

        newsRepository.delete(news);
    }

    private void validateOwnership(News news, UUID requesterId, boolean isAdmin) throws AccessDeniedException {
        if (isAdmin) return; // Admin pode tudo

        if (!news.getAuthor().getId().equals(requesterId)) {
            throw new AccessDeniedException("Você só pode alterar notícias criadas por você.");
        }
    }

    // Lógica para gerar slug (ex: "Festa Junina" -> "festa-junina") [cite: 276]
    private String generateUniqueSlug(String title) {
        String baseSlug = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");

        String uniqueSlug = baseSlug;
        int counter = 1;

        // Garante unicidade adicionando sufixo numérico
        while (newsRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }
        return uniqueSlug;
    }
}