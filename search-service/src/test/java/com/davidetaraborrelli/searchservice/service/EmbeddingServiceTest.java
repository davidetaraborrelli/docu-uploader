package com.davidetaraborrelli.searchservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmbeddingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private EmbeddingService embeddingService;

    @BeforeEach
    void setUp() {
        embeddingService = new EmbeddingService(
                restTemplate,
                "http://localhost:11434",
                "nomic-embed-text"
        );
    }

    @Test
    void generateEmbedding_success() {
        Map<String, Object> response = Map.of(
                "embedding", List.of(0.1, 0.2, 0.3)
        );
        when(restTemplate.postForObject(
                eq("http://localhost:11434/api/embeddings"),
                any(),
                eq(Map.class)
        )).thenReturn(response);

        List<Float> result = embeddingService.generateEmbedding("test text");

        assertEquals(3, result.size());
        assertEquals(0.1f, result.get(0), 0.001);
        assertEquals(0.2f, result.get(1), 0.001);
        assertEquals(0.3f, result.get(2), 0.001);
    }

    @Test
    void generateEmbedding_nullResponse_throws() {
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                embeddingService.generateEmbedding("test")
        );
    }

    @Test
    void generateEmbedding_noEmbeddingKey_throws() {
        Map<String, Object> response = Map.of("other", "data");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        assertThrows(RuntimeException.class, () ->
                embeddingService.generateEmbedding("test")
        );
    }
}
