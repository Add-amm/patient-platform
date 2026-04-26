package com.chronic.vitals.messaging;

import com.chronic.vitals.dto.VitalSignEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VitalsMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.vitals}")
    private String vitalsExchange;

    @Value("${rabbitmq.routing-key.vitals}")
    private String vitalsRoutingKey;

    /**
     * Publie un événement VitalSign vers le rules-engine-service via RabbitMQ.
     */
    public void publishVitalSignEvent(VitalSignEvent event) {
        log.info("Publication de l'événement RabbitMQ -> exchange={}, routingKey={}, patientId={}",
                vitalsExchange, vitalsRoutingKey, event.getPatientId());
        rabbitTemplate.convertAndSend(vitalsExchange, vitalsRoutingKey, event);
    }
}