package com.davidetaraborrelli.notificationservice.messaging;

import com.davidetaraborrelli.notificationservice.config.RabbitConfig;
import com.davidetaraborrelli.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void handleDocumentEvent(DocumentEvent event) {
        log.info("Evento ricevuto: {} per documento {} dell'utente {}",
                event.eventType(), event.documentId(), event.userId());

        String message = switch (event.eventType()) {
            case "UPLOAD_COMPLETED" ->
                    "Il documento '" + event.documentTitle() + "' è stato caricato con successo.";
            case "INDEXING_COMPLETED" ->
                    "Il documento '" + event.documentTitle() + "' è stato indicizzato.";
            default ->
                    "Evento: " + event.eventType() + " per il documento '" + event.documentTitle() + "'.";
        };

        notificationService.createNotification(event.userId(), event.eventType(), message);
    }
}
