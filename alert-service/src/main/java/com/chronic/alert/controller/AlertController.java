package com.chronic.alert.controller;

import com.chronic.alert.dto.AlertRecordResponse;
import com.chronic.alert.model.AlertSeverity;
import com.chronic.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Slf4j
public class AlertController {

    private final AlertService alertService;

    /**
     * GET /api/alerts/{patientId}
     * Retourne l'historique complet des alertes d'un patient.
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<List<AlertRecordResponse>> getAlertsByPatient(
            @PathVariable String patientId) {
        log.info("GET /api/alerts/{}", patientId);
        return ResponseEntity.ok(alertService.getAlertsByPatient(patientId));
    }

    /**
     * GET /api/alerts/{patientId}/severity/{severity}
     * Retourne les alertes d'un patient filtrées par niveau de sévérité.
     * Valeurs possibles : LOW, MEDIUM, HIGH, CRITICAL
     */
    @GetMapping("/{patientId}/severity/{severity}")
    public ResponseEntity<List<AlertRecordResponse>> getAlertsByPatientAndSeverity(
            @PathVariable String patientId,
            @PathVariable AlertSeverity severity) {
        log.info("GET /api/alerts/{}/severity/{}", patientId, severity);
        return ResponseEntity.ok(alertService.getAlertsByPatientAndSeverity(patientId, severity));
    }
}