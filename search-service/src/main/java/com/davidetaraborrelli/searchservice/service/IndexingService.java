package com.davidetaraborrelli.searchservice.service;

import com.davidetaraborrelli.searchservice.messaging.IndexingEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IndexingService {

    private final RestTemplate restTemplate;
    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;
    private final IndexingEventPublisher eventPublisher;
    private final String documentServiceBaseUrl;

    public IndexingService(
            RestTemplate restTemplate,
            EmbeddingService embeddingService,
            QdrantService qdrantService,
            IndexingEventPublisher eventPublisher,
            @Value("${services.document.base-url}") String documentServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.embeddingService = embeddingService;
        this.qdrantService = qdrantService;
        this.eventPublisher = eventPublisher;
        this.documentServiceBaseUrl = documentServiceBaseUrl;
    }

    @SuppressWarnings("unchecked")
    public void indexDocument(Long documentId, Long userId, String title) {
        try {
            log.info("Inizio indicizzazione documento {}", documentId);

            // 1. Fetch contenuto da document-service (API interna)
            String url = documentServiceBaseUrl + "/internal/documents/" + documentId + "/content";
            Map<String, Object> docContent = restTemplate.getForObject(url, Map.class);

            if (docContent == null || docContent.get("content") == null) {
                log.error("Contenuto non trovato per documento {}", documentId);
                return;
            }

            String content = (String) docContent.get("content");
            String docTitle = (String) docContent.get("title");

            // 2. Genera embedding via Ollama
            List<Float> embedding = embeddingService.generateEmbedding(content);

            // 3. Salva in Qdrant con payload metadata
            Map<String, Object> payload = Map.of(
                    "documentId", documentId,
                    "userId", userId,
                    "title", docTitle != null ? docTitle : title
            );
            qdrantService.upsertPoint(documentId, embedding, payload);

            // 4. Pubblica evento indicizzazione completata
            eventPublisher.publishIndexingCompleted(userId, documentId, title);

            log.info("Indicizzazione completata per documento {}", documentId);

        } catch (Exception e) {
            log.error("Errore durante l'indicizzazione del documento {}: {}", documentId, e.getMessage(), e);
        }
    }
}
