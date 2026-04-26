package com.chronic.rules.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service de résolution du contact médical responsable d'un patient.
 *
 * En production, ce service interrogerait le patient-profile-service
 * via une API REST ou un cache. Pour cette démonstration, une
 * correspondance statique est utilisée.
 */
@Service
@Slf4j
public class PatientContactService {

    /**
     * Retourne l'email du médecin ou de l'équipe d'astreinte responsable
     * du patient identifié par patientId.
     */
    public String getResponsibleDoctorEmail(String patientId) {
        // Simulation : en production, appel REST vers patient-profile-service
        log.debug("Résolution de l'email du médecin pour le patient {}", patientId);

        return switch (patientId) {
            case "PAT-001" -> "adamsorouri@gmail.com";
            case "PAT-002" -> "bamohameddouae@gmail.com";
            case "PAT-003" -> "bamohameddouae@gmail.com";
            default -> {
                log.warn("Patient {} inconnu, notification envoyée à l'équipe d'astreinte", patientId);
                yield "adamsorouri@gmail.com";
            }
        };
    }
}