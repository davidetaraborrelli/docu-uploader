package com.davidetaraborrelli.documentservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "document_contents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentContent {

    @Id
    private String id;

    private Long documentId;

    private String content;

    public DocumentContent(String content) {
        this.content = content;
    }
}
