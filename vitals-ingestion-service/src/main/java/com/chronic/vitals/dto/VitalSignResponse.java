package com.chronic.vitals.dto;

import com.chronic.vitals.model.DataSource;
import com.chronic.vitals.model.VitalType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSignResponse {

    private Long id;
    private String patientId;
    private VitalType type;
    private Double value;
    private String unit;
    private DataSource source;
    private LocalDateTime measuredAt;
    private LocalDateTime receivedAt;
    private String notes;
}