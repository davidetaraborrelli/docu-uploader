package com.davidetaraborrelli.searchservice.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class QdrantService {

    private final RestTemplate restTemplate;
    private final String qdrantBaseUrl;
    private final String collectionName;
    private final int vectorSize;

    public QdrantService(
            RestTemplate restTemplate,
            @Value("${services.qdrant.base-url}") String qdrantBaseUrl,
            @Value("${services.qdrant.collection-name}") String collectionName,
            @Value("${services.qdrant.vector-size}") int vectorSize) {
        this.restTemplate = restTemplate;
        this.qdrantBaseUrl = qdrantBaseUrl;
        this.collectionName = collectionName;
        this.vectorSize = vectorSize;
    }

    @PostConstruct
    public void initCollection() {
        try {
            restTemplate.getForObject(
                    qdrantBaseUrl + "/collections/" + collectionName, Map.class);
            log.info("Collezione Qdrant '{}' gia esistente", collectionName);
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Creazione collezione Qdrant '{}'...", collectionName);
            Map<String, Object> body = Map.of(
                    "vectors", Map.of(
                            "size", vectorSize,
                            "distance", "Cosine"
                    )
            );
            restTemplate.put(qdrantBaseUrl + "/collections/" + collectionName, body);
            log.info("Collezione '{}' creata con successo", collectionName);
        }
    }

    public void upsertPoint(Long documentId, List<Float> vector, Map<String, Object> payload) {
        String url = qdrantBaseUrl + "/collections/" + collectionName + "/points";
        Map<String, Object> point = Map.of(
                "id", documentId,
                "vector", vector,
                "payload", payload
        );
        Map<String, Object> body = Map.of("points", List.of(point));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        log.info("Punto inserito in Qdrant per documento {}", documentId);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> search(List<Float> queryVector, int limit) {
        String url = qdrantBaseUrl + "/collections/" + collectionName + "/points/search";
        Map<String, Object> body = Map.of(
                "vector", queryVector,
                "limit", limit,
                "with_payload", true
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getBody() == null || !response.getBody().containsKey("result")) {
            return List.of();
        }

        return (List<Map<String, Object>>) response.getBody().get("result");
    }

    public void deletePoint(Long documentId) {
        String url = qdrantBaseUrl + "/collections/" + collectionName + "/points/delete";
        Map<String, Object> body = Map.of(
                "points", List.of(documentId)
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        log.info("Punto eliminato da Qdrant per documento {}", documentId);
    }
}
