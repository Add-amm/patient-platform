# 🏥 Plateforme de Suivi des Patients Chroniques

Microservices Spring Boot pour le suivi à domicile des patients atteints de maladies chroniques (diabète, hypertension, insuffisance cardiaque).

---

## Architecture des services implémentés

```
┌──────────────────────────┐
│  vitals-ingestion-service │  :8081
│  (API REST réception)     │
└────────────┬─────────────┘
             │ publie VitalSignEvent
             ▼
        [RabbitMQ]
        vitals.queue
             │
             ▼
┌──────────────────────────┐
│   rules-engine-service   │  :8082
│   (moteur de règles)     │
└────────────┬─────────────┘
             │ publie AlertEvent si règle violée
             ▼
        [RabbitMQ]
        alert.queue
             │
             ▼
┌──────────────────────────┐
│      alert-service       │  :8083
│  (notification par email)│
└──────────────────────────┘
```

---

## Prérequis

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

---

## Lancer le projet avec Docker Compose

### 1. Configurer les variables d'environnement SMTP

Éditez `docker-compose.yml` et renseignez vos identifiants SMTP :

```yaml
MAIL_HOST: smtp.gmail.com
MAIL_PORT: 587
MAIL_USERNAME: votre-email@gmail.com
MAIL_PASSWORD: votre-app-password   # Mot de passe d'application Google
MAIL_FROM: alertes@clinique.fr
```

> **Gmail** : activez l'authentification à 2 facteurs puis générez un
> [mot de passe d'application](https://myaccount.google.com/apppasswords).

### 2. Démarrer tous les services

```bash
docker-compose up --build
```

| Service                  | URL                              |
|--------------------------|----------------------------------|
| vitals-ingestion-service | http://localhost:8081            |
| rules-engine-service     | http://localhost:8082            |
| alert-service            | http://localhost:8083            |
| RabbitMQ Management UI   | http://localhost:15672 (guest/guest) |

---

## Lancer en local (sans Docker)

Démarrez RabbitMQ localement ou via Docker seul :

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3.13-management-alpine
```

Puis dans chaque dossier de service :

```bash
mvn spring-boot:run
```

---

## API REST

### vitals-ingestion-service (port 8081)

#### Soumettre une mesure vitale
```http
POST /api/vitals
Content-Type: application/json

{
  "patientId": "PAT-001",
  "type": "HEART_RATE",
  "value": 155,
  "unit": "bpm",
  "source": "IOT",
  "measuredAt": "2025-01-15T10:30:00"
}
```

Types de mesure disponibles :
- `HEART_RATE`
- `BLOOD_PRESSURE_SYSTOLIC`
- `BLOOD_PRESSURE_DIASTOLIC`
- `GLUCOSE`
- `OXYGEN_SATURATION`
- `TEMPERATURE`

Sources : `API`, `IOT`, `MANUAL`

#### Consulter les mesures d'un patient
```http
GET /api/vitals/{patientId}
```

---

### alert-service (port 8083)

#### Historique des alertes d'un patient
```http
GET /api/alerts/{patientId}
```

#### Alertes filtrées par sévérité
```http
GET /api/alerts/{patientId}/severity/{severity}
```
Sévérités : `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

---

## Seuils d'alerte (rules-engine-service)

| Mesure                    | Seuil                  | Sévérité  |
|---------------------------|------------------------|-----------|
| Fréquence cardiaque       | < 40 bpm               | CRITICAL  |
| Fréquence cardiaque       | < 50 bpm               | HIGH      |
| Fréquence cardiaque       | > 100 bpm              | MEDIUM    |
| Fréquence cardiaque       | > 150 bpm              | CRITICAL  |
| Pression systolique       | < 90 mmHg              | HIGH      |
| Pression systolique       | > 140 mmHg             | MEDIUM    |
| Pression systolique       | > 180 mmHg             | CRITICAL  |
| Pression diastolique      | > 90 mmHg              | MEDIUM    |
| Pression diastolique      | > 120 mmHg             | CRITICAL  |
| Glycémie                  | < 54 mg/dL             | CRITICAL  |
| Glycémie                  | < 70 mg/dL             | HIGH      |
| Glycémie                  | > 180 mg/dL            | MEDIUM    |
| Glycémie                  | > 300 mg/dL            | CRITICAL  |
| SpO2                      | < 94 %                 | HIGH      |
| SpO2                      | < 90 %                 | CRITICAL  |
| Température               | < 35 °C                | HIGH      |
| Température               | > 38 °C                | LOW       |
| Température               | > 39.5 °C              | MEDIUM    |
| Température               | > 40.5 °C              | CRITICAL  |

---

## Exemple de flux complet

```bash
# 1. Envoyer une mesure critique (tachycardie sévère)
curl -X POST http://localhost:8081/api/vitals \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "PAT-001",
    "type": "HEART_RATE",
    "value": 160,
    "unit": "bpm",
    "source": "IOT"
  }'

# → rules-engine-service évalue la mesure
# → règle HEART_RATE_CRITICAL_HIGH déclenchée (>150 bpm, CRITICAL)
# → alert-service envoie un email à dr.martin@clinique.fr

# 2. Consulter les alertes générées
curl http://localhost:8083/api/alerts/PAT-001

# 3. Filtrer les alertes critiques uniquement
curl http://localhost:8083/api/alerts/PAT-001/severity/CRITICAL
```

---

## Structure du projet

```
chronic-patient-platform/
├── docker-compose.yml
├── vitals-ingestion-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/chronic/vitals/
│       ├── VitalsIngestionApplication.java
│       ├── controller/
│       │   ├── VitalsIngestionController.java
│       │   └── GlobalExceptionHandler.java
│       ├── service/
│       │   ├── VitalsIngestionService.java
│       │   └── VitalSignRepository.java
│       ├── messaging/
│       │   └── VitalsMessagePublisher.java
│       ├── config/
│       │   └── RabbitMQConfig.java
│       ├── model/
│       │   ├── VitalSign.java
│       │   ├── VitalType.java
│       │   └── DataSource.java
│       └── dto/
│           ├── VitalSignRequest.java
│           ├── VitalSignResponse.java
│           └── VitalSignEvent.java
├── rules-engine-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/chronic/rules/
│       ├── RulesEngineApplication.java
│       ├── messaging/
│       │   ├── VitalsMessageConsumer.java
│       │   └── AlertMessagePublisher.java
│       ├── service/
│       │   ├── RulesEvaluationService.java
│       │   └── PatientContactService.java
│       ├── config/
│       │   └── RabbitMQConfig.java
│       ├── rules/
│       │   ├── AlertRule.java
│       │   └── AlertRulesRegistry.java
│       ├── model/
│       │   ├── VitalType.java
│       │   └── AlertSeverity.java
│       └── dto/
│           ├── VitalSignEvent.java
│           └── AlertEvent.java
└── alert-service/
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/chronic/alert/
        ├── AlertApplication.java
        ├── controller/
        │   └── AlertController.java
        ├── service/
        │   ├── AlertService.java
        │   └── AlertRecordRepository.java
        ├── messaging/
        │   └── AlertMessageConsumer.java
        ├── email/
        │   └── EmailAlertService.java
        ├── config/
        │   └── RabbitMQConfig.java
        ├── model/
        │   ├── AlertRecord.java
        │   ├── AlertSeverity.java
        │   ├── AlertStatus.java
        │   └── VitalType.java
        └── dto/
            ├── AlertEvent.java
            └── AlertRecordResponse.java
```
