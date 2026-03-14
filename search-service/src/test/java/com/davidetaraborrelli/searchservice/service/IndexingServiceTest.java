package com.davidetaraborrelli.searchservice.service;

import com.davidetaraborrelli.searchservice.messaging.IndexingEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EmbeddingService embeddingService;

    @Mock
    private QdrantService qdrantService;

    @Mock
    private IndexingEventPublisher eventPublisher;

    private IndexingService indexingService;

    @BeforeEach
    void setUp() {
        indexingService = new IndexingService(
                restTemplate,
                embeddingService,
                qdrantService,
                eventPublisher,
                "http://localhost:8082"
        );
    }

    @Test
    void indexDocument_success() {
        Map<String, Object> docContent = new HashMap<>();
        docContent.put("content", "contenuto del documento");
        docContent.put("title", "Titolo Doc");

        when(restTemplate.getForObject(
                eq("http://localhost:8082/internal/documents/1/content"),
                eq(Map.class)
        )).thenReturn(docContent);

        when(embeddingService.generateEmbedding("contenuto del documento"))
                .thenReturn(List.of(0.1f, 0.2f, 0.3f));

        indexingService.indexDocument(1L, 2L, "Titolo Doc");

        verify(qdrantService).upsertPoint(eq(1L), anyList(), anyMap());
        verify(eventPublisher).publishIndexingCompleted(2L, 1L, "Titolo Doc");
    }

    @Test
    void indexDocument_nullContent_doesNotIndex() {
        Map<String, Object> docContent = new HashMap<>();
        docContent.put("content", null);

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(docContent);

        indexingService.indexDocument(1L, 2L, "Title");

        verify(embeddingService, never()).generateEmbedding(anyString());
        verify(qdrantService, never()).upsertPoint(anyLong(), anyList(), anyMap());
    }

    @Test
    void indexDocument_nullResponse_doesNotIndex() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(null);

        indexingService.indexDocument(1L, 2L, "Title");

        verify(embeddingService, never()).generateEmbedding(anyString());
        verify(qdrantService, never()).upsertPoint(anyLong(), anyList(), anyMap());
    }

    @Test
    void indexDocument_exceptionCaught_doesNotPropagate() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        assertDoesNotThrow(() ->
                indexingService.indexDocument(1L, 2L, "Title")
        );

        verify(eventPublisher, never()).publishIndexingCompleted(anyLong(), anyLong(), anyString());
    }
}
