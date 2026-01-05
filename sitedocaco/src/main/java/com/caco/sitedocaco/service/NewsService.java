package com.caco.sitedocaco.service;



import com.caco.sitedocaco.dto.response.NewsSummaryDTO;
import com.caco.sitedocaco.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public List<NewsSummaryDTO> getLatestNews(int limit) {
        return newsRepository.findAllSummaries(PageRequest.of(0, limit)).getContent();
    }
}