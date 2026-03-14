package com.davidetaraborrelli.searchservice.service;

import com.davidetaraborrelli.searchservice.dto.SearchResultItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;

    @SuppressWarnings("unchecked")
    public List<SearchResultItem> search(String query, int limit) {
        log.info("Ricerca semantica per query: '{}'", query);

        // 1. Genera embedding per la query
        List<Float> queryEmbedding = embeddingService.generateEmbedding(query);

        // 2. Cerca in Qdrant (ricerca globale su tutti i documenti)
        List<Map<String, Object>> results = qdrantService.search(queryEmbedding, limit);

        // 3. Mappa risultati in DTO
        return results.stream().map(result -> {
            Map<String, Object> payload = (Map<String, Object>) result.get("payload");
            Number score = (Number) result.get("score");
            Number docId = (Number) payload.get("documentId");
            Number userId = (Number) payload.get("userId");
            String title = (String) payload.get("title");

            return new SearchResultItem(
                    docId.longValue(),
                    title,
                    score.floatValue(),
                    userId.longValue()
            );
        }).toList();
    }
}
