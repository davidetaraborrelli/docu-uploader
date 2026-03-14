package com.davidetaraborrelli.searchservice.service;

import com.davidetaraborrelli.searchservice.dto.SearchResultItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private EmbeddingService embeddingService;

    @Mock
    private QdrantService qdrantService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void search_success_returnsResults() {
        when(embeddingService.generateEmbedding("microservizi"))
                .thenReturn(List.of(0.1f, 0.2f, 0.3f));

        Map<String, Object> qdrantResult = Map.of(
                "score", 0.95,
                "payload", Map.of("documentId", 1, "title", "Test Doc", "userId", 2)
        );
        when(qdrantService.search(anyList(), eq(5)))
                .thenReturn(List.of(qdrantResult));

        List<SearchResultItem> results = searchService.search("microservizi", 5);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).documentId());
        assertEquals("Test Doc", results.get(0).title());
        assertEquals(0.95f, results.get(0).score(), 0.001);
        assertEquals(2L, results.get(0).userId());
    }

    @Test
    void search_noResults_returnsEmptyList() {
        when(embeddingService.generateEmbedding("nessun risultato"))
                .thenReturn(List.of(0.1f, 0.2f));
        when(qdrantService.search(anyList(), eq(5)))
                .thenReturn(List.of());

        List<SearchResultItem> results = searchService.search("nessun risultato", 5);

        assertTrue(results.isEmpty());
    }

    @Test
    void search_multipleResults_mapsCorrectly() {
        when(embeddingService.generateEmbedding("query"))
                .thenReturn(List.of(0.1f));

        Map<String, Object> result1 = Map.of(
                "score", 0.95,
                "payload", Map.of("documentId", 1, "title", "Doc 1", "userId", 10)
        );
        Map<String, Object> result2 = Map.of(
                "score", 0.80,
                "payload", Map.of("documentId", 2, "title", "Doc 2", "userId", 20)
        );
        when(qdrantService.search(anyList(), eq(10)))
                .thenReturn(List.of(result1, result2));

        List<SearchResultItem> results = searchService.search("query", 10);

        assertEquals(2, results.size());
        assertEquals("Doc 1", results.get(0).title());
        assertEquals("Doc 2", results.get(1).title());
    }
}
