package com.davidetaraborrelli.searchservice.messaging;

import com.davidetaraborrelli.searchservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishIndexingCompleted(Long userId, Long documentId, String title) {
        DocumentEvent event = DocumentEvent.indexingCompleted(userId, documentId, title);
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                "document.indexed",
                event
        );
        log.info("Evento pubblicato: document.indexed per documento {} dell'utente {}", documentId, userId);
    }
}
