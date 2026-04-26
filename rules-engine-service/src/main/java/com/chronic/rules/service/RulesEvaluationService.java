package com.chronic.rules.service;

import com.chronic.rules.dto.AlertEvent;
import com.chronic.rules.dto.VitalSignEvent;
import com.chronic.rules.messaging.AlertMessagePublisher;
import com.chronic.rules.rules.AlertRule;
import com.chronic.rules.rules.AlertRulesRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Évalue les règles d'alerte pour chaque mesure vitale reçue.
 * Pour chaque règle violée, publie un AlertEvent vers l'alert-service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RulesEvaluationService {

    private final AlertRulesRegistry rulesRegistry;
    private final AlertMessagePublisher alertPublisher;
    private final PatientContactService patientContactService;

    /**
     * Point d'entrée principal : évalue toutes les règles applicables
     * pour l'événement VitalSign reçu.
     */
    public void evaluate(VitalSignEvent event) {
        log.info("Évaluation des règles pour patientId={}, type={}, valeur={}",
                event.getPatientId(), event.getType(), event.getValue());

        List<AlertRule> triggeredRules = findTriggeredRules(event);

        if (triggeredRules.isEmpty()) {
            log.debug("Aucune règle déclenchée pour patientId={}, type={}, valeur={}",
                    event.getPatientId(), event.getType(), event.getValue());
            return;
        }

        log.warn("{} règle(s) déclenchée(s) pour patientId={}", triggeredRules.size(), event.getPatientId());

        for (AlertRule rule : triggeredRules) {
            AlertEvent alertEvent = buildAlertEvent(event, rule);
            alertPublisher.publishAlertEvent(alertEvent);
            log.warn("Alerte publiée : règle={}, sévérité={}, patient={}",
                    rule.getName(), rule.getSeverity(), event.getPatientId());
        }
    }

    /**
     * Retourne toutes les règles violées pour l'événement donné.
     */
    private List<AlertRule> findTriggeredRules(VitalSignEvent event) {
        List<AlertRule> triggered = new ArrayList<>();

        for (AlertRule rule : rulesRegistry.getAllRules()) {
            // Seules les règles du même type de mesure sont vérifiées
            if (!rule.getVitalType().equals(event.getType())) {
                continue;
            }

            boolean violated = 
                (rule.getMinValue() == null || event.getValue() >= rule.getMinValue()) &&
                (rule.getMaxValue() == null || event.getValue() <= rule.getMaxValue());

            if (violated) {
                triggered.add(rule);
            }
        }

        return triggered;
    }

    /**
     * Construit un AlertEvent à partir de la règle déclenchée et de l'événement vital.
     */
    private AlertEvent buildAlertEvent(VitalSignEvent event, AlertRule rule) {
        String message = String.format(rule.getMessageTemplate(), event.getValue());
        String recipientEmail = patientContactService.getResponsibleDoctorEmail(event.getPatientId());

        return AlertEvent.builder()
                .patientId(event.getPatientId())
                .vitalType(event.getType())
                .measuredValue(event.getValue())
                .unit(event.getUnit())
                .severity(rule.getSeverity())
                .ruleName(rule.getName())
                .message(message)
                .triggeredAt(LocalDateTime.now())
                .recipientEmail(recipientEmail)
                .build();
    }
}