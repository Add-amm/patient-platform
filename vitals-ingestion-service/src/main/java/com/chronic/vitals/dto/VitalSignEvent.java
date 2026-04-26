package com.chronic.vitals.dto;

import com.chronic.vitals.model.DataSource;
import com.chronic.vitals.model.VitalType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Événement envoyé au rules-engine-service via RabbitMQ
 * après réception d'une nouvelle mesure vitale.
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
    private DataSource source;
    private LocalDateTime measuredAt;
}