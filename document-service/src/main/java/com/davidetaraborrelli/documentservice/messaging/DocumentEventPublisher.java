package com.davidetaraborrelli.documentservice.messaging;

import com.davidetaraborrelli.documentservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUploadCompleted(Long userId, Long documentId, String title) {
        DocumentEvent event = DocumentEvent.uploadCompleted(userId, documentId, title);
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                "document.uploaded",
                event
        );
        log.info("Evento pubblicato: document.uploaded per documento {} dell'utente {}", documentId, userId);
    }
}
