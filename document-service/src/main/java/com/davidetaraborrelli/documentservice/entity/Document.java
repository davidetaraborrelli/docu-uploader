package com.davidetaraborrelli.documentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "mongo_content_id")
    private String mongoContentId;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Document(Long userId, String title, String mongoContentId, Long fileSize, String mimeType) {
        this.userId = userId;
        this.title = title;
        this.mongoContentId = mongoContentId;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
    }
}
