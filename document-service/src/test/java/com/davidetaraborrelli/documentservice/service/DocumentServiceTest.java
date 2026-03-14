package com.davidetaraborrelli.documentservice.service;

import com.davidetaraborrelli.documentservice.dto.DocumentResponse;
import com.davidetaraborrelli.documentservice.dto.DocumentUploadRequest;
import com.davidetaraborrelli.documentservice.entity.Document;
import com.davidetaraborrelli.documentservice.entity.DocumentContent;
import com.davidetaraborrelli.documentservice.messaging.DocumentEventPublisher;
import com.davidetaraborrelli.documentservice.repository.jpa.DocumentRepository;
import com.davidetaraborrelli.documentservice.repository.mongo.DocumentContentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentContentRepository contentRepository;

    @Mock
    private DocumentEventPublisher eventPublisher;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void upload_success() {
        DocumentUploadRequest request = new DocumentUploadRequest("test.txt", "contenuto del documento");

        DocumentContent mongoContent = new DocumentContent("contenuto del documento");
        mongoContent.setId("mongo-id-123");
        when(contentRepository.save(any(DocumentContent.class))).thenReturn(mongoContent);

        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> {
            Document doc = invocation.getArgument(0);
            doc.setId(1L);
            return doc;
        });

        DocumentResponse response = documentService.upload(1L, request);

        assertEquals(1L, response.id());
        assertEquals("test.txt", response.title());
        assertEquals("contenuto del documento", response.content());
        verify(eventPublisher).publishUploadCompleted(1L, 1L, "test.txt");
        verify(contentRepository, times(2)).save(any(DocumentContent.class));
    }

    @Test
    void listByUser_returnsDocuments() {
        Document doc = new Document(1L, "doc1", "mongo-id-1", 100L, "text/plain");
        doc.setId(1L);
        when(documentRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(doc));

        List<DocumentResponse> result = documentService.listByUser(1L);

        assertEquals(1, result.size());
        assertEquals("doc1", result.get(0).title());
    }

    @Test
    void getByIdAndUser_notFound_throws() {
        when(documentRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> documentService.getByIdAndUser(99L, 1L));
    }

    @Test
    void delete_success() {
        Document doc = new Document(1L, "doc1", "mongo-id-1", 100L, "text/plain");
        doc.setId(1L);
        when(documentRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(doc));

        documentService.delete(1L, 1L);

        verify(contentRepository).deleteById("mongo-id-1");
        verify(documentRepository).delete(doc);
    }

    @Test
    void delete_notOwner_throws() {
        when(documentRepository.findByIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> documentService.delete(1L, 2L));
    }
}
