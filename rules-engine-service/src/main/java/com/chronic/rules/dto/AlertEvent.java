package com.chronic.rules.dto;

import com.chronic.rules.model.AlertSeverity;
import com.chronic.rules.model.VitalType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent implements Serializable {

    private String patientId;
    private VitalType vitalType;
    private Double measuredValue;
    private String unit;
    private AlertSeverity severity;
    private String ruleName;
    private String message;
    private LocalDateTime triggeredAt;
    private String recipientEmail;
}