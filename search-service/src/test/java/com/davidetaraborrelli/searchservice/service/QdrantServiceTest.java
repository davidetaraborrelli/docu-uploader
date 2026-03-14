package com.davidetaraborrelli.searchservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QdrantServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private QdrantService qdrantService;

    @BeforeEach
    void setUp() {
        qdrantService = new QdrantService(
                restTemplate,
                "http://localhost:6333",
                "documents",
                768
        );
    }

    @Test
    void initCollection_alreadyExists() {
        when(restTemplate.getForObject(
                eq("http://localhost:6333/collections/documents"),
                eq(Map.class)
        )).thenReturn(Map.of("result", "exists"));

        qdrantService.initCollection();

        verify(restTemplate, never()).put(anyString(), any());
    }

    @Test
    void initCollection_notFound_createsCollection() {
        when(restTemplate.getForObject(
                eq("http://localhost:6333/collections/documents"),
                eq(Map.class)
        )).thenThrow(HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found",
                HttpHeaders.EMPTY, new byte[0], null
        ));

        qdrantService.initCollection();

        verify(restTemplate).put(
                eq("http://localhost:6333/collections/documents"),
                any()
        );
    }

    @Test
    void upsertPoint_callsRestTemplate() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Map.of()));

        qdrantService.upsertPoint(1L, List.of(0.1f, 0.2f), Map.of("documentId", 1L));

        verify(restTemplate).exchange(
                eq("http://localhost:6333/collections/documents/points"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }

    @Test
    void search_returnsResults() {
        Map<String, Object> result = Map.of(
                "score", 0.95,
                "payload", Map.of("documentId", 1, "title", "Test Doc", "userId", 2)
        );
        ResponseEntity<Map> response = ResponseEntity.ok(Map.of("result", List.of(result)));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(response);

        List<Map<String, Object>> results = qdrantService.search(List.of(0.1f, 0.2f), 5);

        assertEquals(1, results.size());
    }

    @Test
    void search_nullBody_returnsEmptyList() {
        ResponseEntity<Map> response = ResponseEntity.ok(null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(response);

        List<Map<String, Object>> results = qdrantService.search(List.of(0.1f), 5);

        assertTrue(results.isEmpty());
    }

    @Test
    void deletePoint_callsRestTemplate() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Map.of()));

        qdrantService.deletePoint(1L);

        verify(restTemplate).exchange(
                eq("http://localhost:6333/collections/documents/points/delete"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }
}
