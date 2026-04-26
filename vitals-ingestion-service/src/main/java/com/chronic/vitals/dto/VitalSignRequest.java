package com.chronic.vitals.dto;

import com.chronic.vitals.model.DataSource;
import com.chronic.vitals.model.VitalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSignRequest {

    @NotBlank(message = "Le patientId est obligatoire")
    private String patientId;

    @NotNull(message = "Le type de mesure est obligatoire")
    private VitalType type;

    @NotNull(message = "La valeur est obligatoire")
    @Positive(message = "La valeur doit être positive")
    private Double value;

    private String unit;

    private DataSource source;

    /** Si null, on utilise l'heure courante */
    private LocalDateTime measuredAt;

    private String notes;
}