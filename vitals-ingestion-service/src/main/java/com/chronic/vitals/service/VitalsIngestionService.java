package com.chronic.vitals.service;

import com.chronic.vitals.dto.VitalSignEvent;
import com.chronic.vitals.dto.VitalSignRequest;
import com.chronic.vitals.dto.VitalSignResponse;
import com.chronic.vitals.messaging.VitalsMessagePublisher;
import com.chronic.vitals.model.DataSource;
import com.chronic.vitals.model.VitalSign;
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
public class VitalsIngestionService {

    private final VitalSignRepository vitalSignRepository;
    private final VitalsMessagePublisher messagePublisher;

    /**
     * Enregistre une nouvelle mesure vitale et publie un événement sur RabbitMQ.
     */
    @Transactional
    public VitalSignResponse ingestVital(VitalSignRequest request) {
        log.info("Réception d'une mesure vitale pour le patient {} : type={}, valeur={}",
                request.getPatientId(), request.getType(), request.getValue());

        VitalSign vitalSign = VitalSign.builder()
                .patientId(request.getPatientId())
                .type(request.getType())
                .value(request.getValue())
                .unit(request.getUnit())
                .source(request.getSource() != null ? request.getSource() : DataSource.MANUAL)
                .measuredAt(request.getMeasuredAt() != null ? request.getMeasuredAt() : LocalDateTime.now())
                .receivedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        VitalSign saved = vitalSignRepository.save(vitalSign);

        // Publier l'événement vers le rules-engine-service
        VitalSignEvent event = VitalSignEvent.builder()
                .vitalSignId(saved.getId())
                .patientId(saved.getPatientId())
                .type(saved.getType())
                .value(saved.getValue())
                .unit(saved.getUnit())
                .source(saved.getSource())
                .measuredAt(saved.getMeasuredAt())
                .build();

        messagePublisher.publishVitalSignEvent(event);
        log.info("Événement publié sur RabbitMQ pour la mesure id={}", saved.getId());

        return toResponse(saved);
    }

    /**
     * Récupère toutes les mesures d'un patient.
     */
    @Transactional(readOnly = true)
    public List<VitalSignResponse> getVitalsByPatient(String patientId) {
        return vitalSignRepository.findByPatientIdOrderByMeasuredAtDesc(patientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VitalSignResponse toResponse(VitalSign v) {
        return VitalSignResponse.builder()
                .id(v.getId())
                .patientId(v.getPatientId())
                .type(v.getType())
                .value(v.getValue())
                .unit(v.getUnit())
                .source(v.getSource())
                .measuredAt(v.getMeasuredAt())
                .receivedAt(v.getReceivedAt())
                .notes(v.getNotes())
                .build();
    }
}