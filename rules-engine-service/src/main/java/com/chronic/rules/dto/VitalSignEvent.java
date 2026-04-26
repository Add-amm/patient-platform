package com.chronic.rules.dto;

import com.chronic.rules.model.VitalType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Événement reçu depuis vitals-ingestion-service via RabbitMQ.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSignEvent implements Serializable {

    private Long vitalSignId;
    private String patientId;
    private VitalType type;
    private Double value;
    private String unit;
    private String source;
    private LocalDateTime measuredAt;
}