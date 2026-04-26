package com.chronic.alert.model;

public enum AlertStatus {
    /** Email envoyé avec succès */
    SENT,
    /** Échec de l'envoi */
    FAILED,
    /** En attente de traitement */
    PENDING
}