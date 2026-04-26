package com.chronic.rules.messaging;

import com.chronic.rules.dto.VitalSignEvent;
import com.chronic.rules.service.RulesEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VitalsMessageConsumer {

    private final RulesEvaluationService rulesEvaluationService;

    /**
     * Consomme les événements VitalSign publiés par vitals-ingestion-service.
     * Déclenche l'évaluation des règles d'alerte.
     */
    @RabbitListener(queues = "${rabbitmq.queue.vitals}")
    public void consumeVitalSignEvent(VitalSignEvent event) {
        log.info("Message reçu depuis vitals.queue : patientId={}, type={}, valeur={}",
                event.getPatientId(), event.getType(), event.getValue());
        try {
            rulesEvaluationService.evaluate(event);
        } catch (Exception e) {
            log.error("Erreur lors de l'évaluation des règles pour patientId={} : {}",
                    event.getPatientId(), e.getMessage(), e);
        }
    }
}