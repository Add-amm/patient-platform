package com.chronic.vitals.controller;

import com.chronic.vitals.dto.VitalSignRequest;
import com.chronic.vitals.dto.VitalSignResponse;
import com.chronic.vitals.service.VitalsIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vitals")
@RequiredArgsConstructor
@Slf4j
public class VitalsIngestionController {

    private final VitalsIngestionService vitalsIngestionService;

    /**
     * POST /api/vitals
     * Soumet une nouvelle mesure vitale (depuis app mobile, IoT ou saisie manuelle).
     */
    @PostMapping
    public ResponseEntity<VitalSignResponse> ingestVital(@Valid @RequestBody VitalSignRequest request) {
        log.info("POST /api/vitals - patientId={}, type={}", request.getPatientId(), request.getType());
        VitalSignResponse response = vitalsIngestionService.ingestVital(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/vitals/{patientId}
     * Récupère l'historique des mesures d'un patient.
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<List<VitalSignResponse>> getVitalsByPatient(@PathVariable String patientId) {
        log.info("GET /api/vitals/{}", patientId);
        return ResponseEntity.ok(vitalsIngestionService.getVitalsByPatient(patientId));
    }
}