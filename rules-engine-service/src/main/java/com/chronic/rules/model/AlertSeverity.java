package com.chronic.rules.model;

public enum AlertSeverity {
    /** Information : valeur limite atteinte mais non critique */
    LOW,
    /** Avertissement : valeur hors seuil, surveillance recommandée */
    MEDIUM,
    /** Urgence : valeur critique, intervention immédiate requise */
    HIGH,
    /** Urgence absolue : risque vital immédiat */
    CRITICAL
}