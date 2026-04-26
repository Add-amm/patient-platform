package com.chronic.rules.rules;

import com.chronic.rules.model.AlertSeverity;
import com.chronic.rules.model.VitalType;
import lombok.*;

/**
 * Définit un seuil d'alerte pour un type de mesure vitale.
 * Une règle se déclenche si la valeur mesurée dépasse minValue ou maxValue.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRule {

    private String name;
    private VitalType vitalType;
    /** Seuil minimum (null = pas de vérification min) */
    private Double minValue;
    /** Seuil maximum (null = pas de vérification max) */
    private Double maxValue;
    private AlertSeverity severity;
    private String messageTemplate;
}