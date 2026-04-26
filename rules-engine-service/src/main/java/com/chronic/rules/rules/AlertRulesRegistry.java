package com.chronic.rules.rules;

import com.chronic.rules.model.AlertSeverity;
import com.chronic.rules.model.VitalType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Registre centralisé de toutes les règles d'alerte métier.
 * Ces seuils sont basés sur les recommandations cliniques standard.
 *
 * Pour une approche production, ces règles peuvent être externalisées
 * dans un fichier YAML ou une base de données.
 */
@Component
public class AlertRulesRegistry {

    public List<AlertRule> getAllRules() {
        return List.of(

            // ── FRÉQUENCE CARDIAQUE (bpm) ─────────────────────────────────────
            AlertRule.builder()
                .name("HEART_RATE_CRITICAL_LOW")
                .vitalType(VitalType.HEART_RATE)
                .maxValue(null).minValue(null)
                .minValue(null)
                .maxValue(40.0)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Bradycardie critique : fréquence cardiaque de %.1f bpm (seuil critique < 40 bpm)")
                .build(),

            AlertRule.builder()
                .name("HEART_RATE_LOW")
                .vitalType(VitalType.HEART_RATE)
                .minValue(40.1)
                .maxValue(50.0)
                .severity(AlertSeverity.HIGH)
                .messageTemplate("Bradycardie : fréquence cardiaque de %.1f bpm (seuil < 50 bpm)")
                .build(),

            AlertRule.builder()
                .name("HEART_RATE_HIGH")
                .vitalType(VitalType.HEART_RATE)
                .minValue(100.0)
                .maxValue(149.9)
                .severity(AlertSeverity.MEDIUM)
                .messageTemplate("Tachycardie : fréquence cardiaque de %.1f bpm (seuil > 100 bpm)")
                .build(),

            AlertRule.builder()
                .name("HEART_RATE_CRITICAL_HIGH")
                .vitalType(VitalType.HEART_RATE)
                .minValue(150.0)
                .maxValue(null)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Tachycardie critique : fréquence cardiaque de %.1f bpm (seuil critique > 150 bpm)")
                .build(),

            // ── PRESSION ARTÉRIELLE SYSTOLIQUE (mmHg) ────────────────────────
            AlertRule.builder()
                .name("BP_SYSTOLIC_LOW")
                .vitalType(VitalType.BLOOD_PRESSURE_SYSTOLIC)
                .minValue(null)
                .maxValue(90.0)
                .severity(AlertSeverity.HIGH)
                .messageTemplate("Hypotension : pression systolique de %.1f mmHg (seuil < 90 mmHg)")
                .build(),

            AlertRule.builder()
                .name("BP_SYSTOLIC_HIGH")
                .vitalType(VitalType.BLOOD_PRESSURE_SYSTOLIC)
                .minValue(140.0)
                .maxValue(179.9)
                .severity(AlertSeverity.MEDIUM)
                .messageTemplate("Hypertension stade 1 : pression systolique de %.1f mmHg (seuil > 140 mmHg)")
                .build(),

            AlertRule.builder()
                .name("BP_SYSTOLIC_CRISIS")
                .vitalType(VitalType.BLOOD_PRESSURE_SYSTOLIC)
                .minValue(180.0)
                .maxValue(null)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Crise hypertensive : pression systolique de %.1f mmHg (seuil critique > 180 mmHg)")
                .build(),

            // ── PRESSION ARTÉRIELLE DIASTOLIQUE (mmHg) ───────────────────────
            AlertRule.builder()
                .name("BP_DIASTOLIC_HIGH")
                .vitalType(VitalType.BLOOD_PRESSURE_DIASTOLIC)
                .minValue(90.0)
                .maxValue(119.9)
                .severity(AlertSeverity.MEDIUM)
                .messageTemplate("Hypertension diastolique : pression diastolique de %.1f mmHg (seuil > 90 mmHg)")
                .build(),

            AlertRule.builder()
                .name("BP_DIASTOLIC_CRISIS")
                .vitalType(VitalType.BLOOD_PRESSURE_DIASTOLIC)
                .minValue(120.0)
                .maxValue(null)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Crise hypertensive diastolique : pression de %.1f mmHg (seuil critique > 120 mmHg)")
                .build(),

            // ── GLYCÉMIE (mg/dL) ──────────────────────────────────────────────
            AlertRule.builder()
                .name("GLUCOSE_CRITICAL_LOW")
                .vitalType(VitalType.GLUCOSE)
                .minValue(null)
                .maxValue(54.0)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Hypoglycémie sévère : glycémie de %.1f mg/dL (seuil critique < 54 mg/dL)")
                .build(),

            AlertRule.builder()
                .name("GLUCOSE_LOW")
                .vitalType(VitalType.GLUCOSE)
                .minValue(54.1)
                .maxValue(70.0)
                .severity(AlertSeverity.HIGH)
                .messageTemplate("Hypoglycémie : glycémie de %.1f mg/dL (seuil < 70 mg/dL)")
                .build(),

            AlertRule.builder()
                .name("GLUCOSE_HIGH")
                .vitalType(VitalType.GLUCOSE)
                .minValue(180.0)
                .maxValue(299.9)
                .severity(AlertSeverity.MEDIUM)
                .messageTemplate("Hyperglycémie : glycémie de %.1f mg/dL (seuil > 180 mg/dL)")
                .build(),

            AlertRule.builder()
                .name("GLUCOSE_CRITICAL_HIGH")
                .vitalType(VitalType.GLUCOSE)
                .minValue(300.0)
                .maxValue(null)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Hyperglycémie sévère : glycémie de %.1f mg/dL (seuil critique > 300 mg/dL)")
                .build(),

            // ── SATURATION EN OXYGÈNE (%) ─────────────────────────────────────
            AlertRule.builder()
                .name("OXYGEN_SATURATION_LOW")
                .vitalType(VitalType.OXYGEN_SATURATION)
                .minValue(90.1)
                .maxValue(94.0)
                .severity(AlertSeverity.HIGH)
                .messageTemplate("Désaturation : SpO2 de %.1f %% (seuil < 94%%)")
                .build(),

            AlertRule.builder()
                .name("OXYGEN_SATURATION_CRITICAL")
                .vitalType(VitalType.OXYGEN_SATURATION)
                .minValue(null)
                .maxValue(90.0)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Hypoxémie critique : SpO2 de %.1f %% (seuil critique < 90%%)")
                .build(),

            // ── TEMPÉRATURE (°C) ──────────────────────────────────────────────
            AlertRule.builder()
                .name("TEMPERATURE_HYPOTHERMIA")
                .vitalType(VitalType.TEMPERATURE)
                .minValue(null)
                .maxValue(35.0)
                .severity(AlertSeverity.HIGH)
                .messageTemplate("Hypothermie : température de %.1f°C (seuil < 35°C)")
                .build(),

            AlertRule.builder()
                .name("TEMPERATURE_FEVER")
                .vitalType(VitalType.TEMPERATURE)
                .minValue(38.0)
                .maxValue(39.4)
                .severity(AlertSeverity.LOW)
                .messageTemplate("Fièvre : température de %.1f°C (seuil > 38°C)")
                .build(),

            AlertRule.builder()
                .name("TEMPERATURE_HIGH_FEVER")
                .vitalType(VitalType.TEMPERATURE)
                .minValue(39.5)
                .maxValue(40.4)
                .severity(AlertSeverity.MEDIUM)
                .messageTemplate("Fièvre élevée : température de %.1f°C (seuil > 39.5°C)")
                .build(),

            AlertRule.builder()
                .name("TEMPERATURE_CRITICAL")
                .vitalType(VitalType.TEMPERATURE)
                .minValue(40.5)
                .maxValue(null)
                .severity(AlertSeverity.CRITICAL)
                .messageTemplate("Hyperthermie critique : température de %.1f°C (seuil critique > 40.5°C)")
                .build()
        );
    }
}