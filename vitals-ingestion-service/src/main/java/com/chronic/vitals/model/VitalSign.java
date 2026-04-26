package com.chronic.vitals.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vital_signs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientId;

    /** Type de mesure : HEART_RATE, BLOOD_PRESSURE_SYSTOLIC, BLOOD_PRESSURE_DIASTOLIC,
     *  GLUCOSE, OXYGEN_SATURATION, TEMPERATURE */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VitalType type;

    @Column(name = "measurement_value", nullable = false)
    private Double value;

    /** Unité de la mesure (bpm, mmHg, mg/dL, %, °C) */
    private String unit;

    /** Source de la saisie : API, IOT, MANUAL */
    @Enumerated(EnumType.STRING)
    private DataSource source;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    private String notes;
}