package com.chronic.alert.dto;

import com.chronic.alert.model.AlertSeverity;
import com.chronic.alert.model.AlertStatus;
import com.chronic.alert.model.VitalType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRecordResponse {

    private Long id;
    private String patientId;
    private VitalType vitalType;
    private Double measuredValue;
    private String unit;
    private AlertSeverity severity;
    private String ruleName;
    private String message;
    private String recipientEmail;
    private AlertStatus status;
    private String errorMessage;
    private LocalDateTime triggeredAt;
    private LocalDateTime processedAt;
}