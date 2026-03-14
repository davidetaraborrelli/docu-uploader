package com.davidetaraborrelli.documentservice.service;

import com.davidetaraborrelli.documentservice.dto.DocumentContentResponse;
import com.davidetaraborrelli.documentservice.dto.DocumentResponse;
import com.davidetaraborrelli.documentservice.dto.DocumentUploadRequest;
import com.davidetaraborrelli.documentservice.entity.Document;
import com.davidetaraborrelli.documentservice.entity.DocumentContent;
import com.davidetaraborrelli.documentservice.messaging.DocumentEventPublisher;
import com.davidetaraborrelli.documentservice.repository.jpa.DocumentRepository;
import com.davidetaraborrelli.documentservice.repository.mongo.DocumentContentRepository;
import com.davidetaraborrelli.common.performance.TracePerformance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentContentRepository contentRepository;
    private final DocumentEventPublisher eventPublisher;

    @TracePerformance(repo = "document", data = "upload")
    public DocumentResponse upload(Long userId, DocumentUploadRequest request) {
        // 1. Salva contenuto in MongoDB
        DocumentContent mongoContent = new DocumentContent(request.content());
        mongoContent = contentRepository.save(mongoContent);

        // 2. Salva metadati in PostgreSQL con riferimento MongoDB
        Document document = new Document(
                userId,
                request.title(),
                mongoContent.getId(),
                (long) request.content().getBytes().length,
                "text/plain"
        );
        document = documentRepository.save(document);

        // 3. Aggiorna back-reference in MongoDB
        mongoContent.setDocumentId(document.getId());
        contentRepository.save(mongoContent);

        // 4. Pubblica evento
        eventPublisher.publishUploadCompleted(userId, document.getId(), document.getTitle());

        return toResponse(document, request.content());
    }

    public List<DocumentResponse> listByUser(Long userId) {
        return documentRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(doc -> toResponse(doc, null))
                .toList();
    }

    @TracePerformance(repo = "document", data = "get")
    public DocumentResponse getByIdAndUser(Long documentId, Long userId) {
        Document document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Documento non trovato"));
        String content = fetchContent(document.getMongoContentId());
        return toResponse(document, content);
    }

    @TracePerformance(repo = "document", data = "delete")
    public void delete(Long documentId, Long userId) {
        Document document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Documento non trovato"));
        if (document.getMongoContentId() != null) {
            contentRepository.deleteById(document.getMongoContentId());
        }
        documentRepository.delete(document);
    }

    public DocumentContentResponse getContentForIndexing(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Documento non trovato"));
        String content = fetchContent(document.getMongoContentId());
        return new DocumentContentResponse(document.getId(), document.getTitle(), content, document.getUserId());
    }

    private String fetchContent(String mongoContentId) {
        if (mongoContentId == null) return null;
        return contentRepository.findById(mongoContentId)
                .map(DocumentContent::getContent)
                .orElse(null);
    }

    private DocumentResponse toResponse(Document doc, String content) {
        return new DocumentResponse(
                doc.getId(),
                doc.getTitle(),
                content,
                doc.getFileSize(),
                doc.getMimeType(),
                doc.getCreatedAt(),
                doc.getUpdatedAt()
        );
    }
}
