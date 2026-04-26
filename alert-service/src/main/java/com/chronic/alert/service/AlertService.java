package com.chronic.alert.service;

import com.chronic.alert.dto.AlertEvent;
import com.chronic.alert.dto.AlertRecordResponse;
import com.chronic.alert.email.EmailAlertService;
import com.chronic.alert.model.AlertRecord;
import com.chronic.alert.model.AlertSeverity;
import com.chronic.alert.model.AlertStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRecordRepository alertRecordRepository;
    private final EmailAlertService emailAlertService;

    /**
     * Traite un événement d'alerte :
     * 1. Persiste l'alerte en base avec statut PENDING
     * 2. Envoie l'email de notification
     * 3. Met à jour le statut (SENT ou FAILED)
     */
    @Transactional
    public void processAlert(AlertEvent event) {
        log.info("Traitement de l'alerte : patientId={}, sévérité={}, règle={}",
                event.getPatientId(), event.getSeverity(), event.getRuleName());

        // 1 – Persistance initiale
        AlertRecord record = AlertRecord.builder()
                .patientId(event.getPatientId())
                .vitalType(event.getVitalType())
                .measuredValue(event.getMeasuredValue())
                .unit(event.getUnit())
                .severity(event.getSeverity())
                .ruleName(event.getRuleName())
                .message(event.getMessage())
                .recipientEmail(event.getRecipientEmail())
                .status(AlertStatus.PENDING)
                .triggeredAt(event.getTriggeredAt() != null ? event.getTriggeredAt() : LocalDateTime.now())
                .processedAt(LocalDateTime.now())
                .build();

        record = alertRecordRepository.save(record);

        // 2 – Envoi de l'email
        try {
            emailAlertService.sendAlertEmail(event);
            record.setStatus(AlertStatus.SENT);
            log.info("Alerte id={} envoyée avec succès à {}", record.getId(), event.getRecipientEmail());
        } catch (Exception e) {
            record.setStatus(AlertStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("Échec d'envoi de l'alerte id={} : {}", record.getId(), e.getMessage(), e);
        }

        // 3 – Mise à jour du statut
        alertRecordRepository.save(record);
    }

    /**
     * Récupère l'historique de toutes les alertes d'un patient.
     */
    @Transactional(readOnly = true)
    public List<AlertRecordResponse> getAlertsByPatient(String patientId) {
        return alertRecordRepository
                .findByPatientIdOrderByTriggeredAtDesc(patientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les alertes d'un patient filtrées par sévérité.
     */
    @Transactional(readOnly = true)
    public List<AlertRecordResponse> getAlertsByPatientAndSeverity(String patientId, AlertSeverity severity) {
        return alertRecordRepository
                .findByPatientIdAndSeverityOrderByTriggeredAtDesc(patientId, severity)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AlertRecordResponse toResponse(AlertRecord r) {
        return AlertRecordResponse.builder()
                .id(r.getId())
                .patientId(r.getPatientId())
                .vitalType(r.getVitalType())
                .measuredValue(r.getMeasuredValue())
                .unit(r.getUnit())
                .severity(r.getSeverity())
                .ruleName(r.getRuleName())
                .message(r.getMessage())
                .recipientEmail(r.getRecipientEmail())
                .status(r.getStatus())
                .errorMessage(r.getErrorMessage())
                .triggeredAt(r.getTriggeredAt())
                .processedAt(r.getProcessedAt())
                .build();
    }
}