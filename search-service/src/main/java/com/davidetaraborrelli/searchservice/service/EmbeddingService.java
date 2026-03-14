package com.davidetaraborrelli.searchservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmbeddingService {

    private final RestTemplate restTemplate;
    private final String ollamaBaseUrl;
    private final String modelName;

    public EmbeddingService(
            RestTemplate restTemplate,
            @Value("${services.ollama.base-url}") String ollamaBaseUrl,
            @Value("${services.ollama.model}") String modelName) {
        this.restTemplate = restTemplate;
        this.ollamaBaseUrl = ollamaBaseUrl;
        this.modelName = modelName;
    }

    @SuppressWarnings("unchecked")
    public List<Float> generateEmbedding(String text) {
        String url = ollamaBaseUrl + "/api/embeddings";
        Map<String, String> request = Map.of("model", modelName, "prompt", text);

        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

        if (response == null || !response.containsKey("embedding")) {
            throw new RuntimeException("Errore nella generazione dell'embedding da Ollama");
        }

        List<Double> embedding = (List<Double>) response.get("embedding");
        log.info("Embedding generato: {} dimensioni", embedding.size());
        return embedding.stream().map(Double::floatValue).toList();
    }
}
