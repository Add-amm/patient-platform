package com.chronic.alert.messaging;

import com.chronic.alert.dto.AlertEvent;
import com.chronic.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertMessageConsumer {

    private final AlertService alertService;

    /**
     * Consomme les événements d'alerte publiés par rules-engine-service.
     * Déclenche l'envoi de l'email de notification.
     */
    @RabbitListener(queues = "${rabbitmq.queue.alert}")
    public void consumeAlertEvent(AlertEvent event) {
        log.info("Message reçu depuis alert.queue : patientId={}, sévérité={}, règle={}",
                event.getPatientId(), event.getSeverity(), event.getRuleName());
        try {
            alertService.processAlert(event);
        } catch (Exception e) {
            log.error("Erreur critique lors du traitement de l'alerte pour patientId={} : {}",
                    event.getPatientId(), e.getMessage(), e);
        }
    }
}