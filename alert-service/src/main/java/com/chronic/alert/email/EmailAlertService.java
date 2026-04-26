package com.chronic.alert.email;

import com.chronic.alert.dto.AlertEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service responsable de l'envoi des emails d'alerte médicale.
 * Utilise Spring Boot Mail (JavaMailSender) et Thymeleaf pour le rendu HTML.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAlertService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${alert.mail.from}")
    private String fromAddress;

    @Value("${alert.mail.from-name}")
    private String fromName;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss");

    /**
     * Envoie un email d'alerte HTML au médecin responsable.
     *
     * @param event l'événement d'alerte à notifier
     * @throws MessagingException en cas d'erreur d'envoi
     */
    public void sendAlertEmail(AlertEvent event) throws MessagingException {
        log.info("Préparation de l'email d'alerte pour {} -> destinataire={}",
                event.getPatientId(), event.getRecipientEmail());

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            helper.setFrom(fromAddress, fromName);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new MessagingException("Erreur d'encodage du nom d'expéditeur", e);
        }

        helper.setTo(event.getRecipientEmail());
        helper.setSubject(buildSubject(event));

        String htmlContent = buildHtmlContent(event);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
        log.info("Email d'alerte envoyé avec succès à {}", event.getRecipientEmail());
    }

    /**
     * Construit l'objet de l'email selon la sévérité de l'alerte.
     */
    private String buildSubject(AlertEvent event) {
        String prefix = switch (event.getSeverity()) {
            case CRITICAL -> "🚨 URGENCE VITALE";
            case HIGH     -> "⚠️ ALERTE HAUTE";
            case MEDIUM   -> "⚡ ALERTE MÉDICALE";
            case LOW      -> "ℹ️ NOTIFICATION";
        };
        return String.format("[%s] Patient %s – %s",
                prefix, event.getPatientId(), event.getMessage());
    }

    /**
     * Génère le contenu HTML de l'email via le template Thymeleaf alert-email.html.
     */
    private String buildHtmlContent(AlertEvent event) {
        Context context = new Context(Locale.FRENCH);
        context.setVariable("patientId",     event.getPatientId());
        context.setVariable("vitalType",     formatVitalType(event.getVitalType().name()));
        context.setVariable("measuredValue", event.getMeasuredValue());
        context.setVariable("unit",          event.getUnit() != null ? event.getUnit() : "");
        context.setVariable("severity",      event.getSeverity().name());
        context.setVariable("severityLabel", formatSeverityLabel(event.getSeverity().name()));
        context.setVariable("severityColor", getSeverityColor(event.getSeverity().name()));
        context.setVariable("ruleName",      event.getRuleName());
        context.setVariable("message",       event.getMessage());
        context.setVariable("triggeredAt",
                event.getTriggeredAt() != null
                        ? event.getTriggeredAt().format(DATE_FORMATTER)
                        : "N/A");

        return templateEngine.process("alert-email", context);
    }

    private String formatVitalType(String type) {
        return switch (type) {
            case "HEART_RATE"              -> "Fréquence cardiaque";
            case "BLOOD_PRESSURE_SYSTOLIC" -> "Pression artérielle systolique";
            case "BLOOD_PRESSURE_DIASTOLIC"-> "Pression artérielle diastolique";
            case "GLUCOSE"                 -> "Glycémie";
            case "OXYGEN_SATURATION"       -> "Saturation en oxygène (SpO2)";
            case "TEMPERATURE"             -> "Température corporelle";
            default                        -> type;
        };
    }

    private String formatSeverityLabel(String severity) {
        return switch (severity) {
            case "CRITICAL" -> "URGENCE VITALE";
            case "HIGH"     -> "ALERTE HAUTE";
            case "MEDIUM"   -> "ALERTE MÉDICALE";
            case "LOW"      -> "INFORMATION";
            default         -> severity;
        };
    }

    private String getSeverityColor(String severity) {
        return switch (severity) {
            case "CRITICAL" -> "#C0392B";
            case "HIGH"     -> "#E67E22";
            case "MEDIUM"   -> "#F39C12";
            case "LOW"      -> "#2980B9";
            default         -> "#7F8C8D";
        };
    }
}