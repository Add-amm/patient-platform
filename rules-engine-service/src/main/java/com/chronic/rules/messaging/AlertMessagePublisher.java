package com.chronic.rules.messaging;

import com.chronic.rules.dto.AlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.alert}")
    private String alertExchange;

    @Value("${rabbitmq.routing-key.alert}")
    private String alertRoutingKey;

    /**
     * Publie un événement d'alerte vers l'alert-service via RabbitMQ.
     */
    public void publishAlertEvent(AlertEvent alertEvent) {
        log.info("Publication alerte -> exchange={}, sévérité={}, patientId={}",
                alertExchange, alertEvent.getSeverity(), alertEvent.getPatientId());
        rabbitTemplate.convertAndSend(alertExchange, alertRoutingKey, alertEvent);
    }
}