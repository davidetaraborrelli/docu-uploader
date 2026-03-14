package com.davidetaraborrelli.documentservice.repository.mongo;

import com.davidetaraborrelli.documentservice.entity.DocumentContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DocumentContentRepository extends MongoRepository<DocumentContent, String> {

    Optional<DocumentContent> findByDocumentId(Long documentId);

    void deleteByDocumentId(Long documentId);
}
