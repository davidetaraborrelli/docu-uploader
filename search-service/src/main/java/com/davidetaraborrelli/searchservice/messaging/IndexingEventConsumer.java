package com.davidetaraborrelli.searchservice.messaging;

import com.davidetaraborrelli.searchservice.config.RabbitConfig;
import com.davidetaraborrelli.searchservice.service.IndexingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexingEventConsumer {

    private final IndexingService indexingService;

    @RabbitListener(queues = RabbitConfig.INDEXING_QUEUE)
    public void handleDocumentUploaded(DocumentEvent event) {
        log.info("Evento ricevuto: {} per documento {} dell'utente {}",
                event.eventType(), event.documentId(), event.userId());

        if ("UPLOAD_COMPLETED".equals(event.eventType())) {
            indexingService.indexDocument(event.documentId(), event.userId(), event.documentTitle());
        }
    }
}
