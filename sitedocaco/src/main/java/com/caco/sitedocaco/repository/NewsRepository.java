package com.caco.sitedocaco.repository;

import com.caco.sitedocaco.dto.response.NewsDetailDTO;
import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.entity.home.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {

    Optional<News> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // Optimized Query: Returns DTOs directly, skipping the heavy 'content' field
    @Query("SELECT new com.caco.sitedocaco.dto.response.NewsSummaryDTO(" +
            "n.id, n.title, n.slug, n.summary, n.coverImage, n.publishDate, n.author.username) " +
            "FROM News n ORDER BY n.publishDate DESC")
    Page<NewsSummaryDTO> findAllSummaries(Pageable pageable);

    // Full news detail by slug (includes content + author avatar)
    @Query("SELECT new com.caco.sitedocaco.dto.response.NewsDetailDTO(" +
            "n.id, n.title, n.slug, n.summary, n.content, n.coverImage, n.publishDate, " +
            "n.author.username, n.author.avatarUrl) " +
            "FROM News n WHERE n.slug = :slug")
    Optional<NewsDetailDTO> findDetailBySlug(@Param("slug") String slug);
}