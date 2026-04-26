package com.chronic.alert.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entité persistée représentant une alerte envoyée ou tentée.
 * Permet de tracer l'historique complet des notifications.
 */
@Entity
@Table(name = "alert_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VitalType vitalType;

    @Column(nullable = false)
    private Double measuredValue;

    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(nullable = false)
    private String ruleName;

    @Column(nullable = false, length = 1000)
    private String message;

    /** Email du destinataire notifié */
    @Column(nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    /** Message d'erreur en cas d'échec d'envoi */
    @Column(length = 500)
    private String errorMessage;

    /** Horodatage du déclenchement de la règle */
    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    /** Horodatage de la tentative d'envoi de la notification */
    @Column(nullable = false)
    private LocalDateTime processedAt;
}