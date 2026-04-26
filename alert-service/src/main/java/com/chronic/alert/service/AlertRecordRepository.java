package com.chronic.alert.service;

import com.chronic.alert.model.AlertRecord;
import com.chronic.alert.model.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long> {

    List<AlertRecord> findByPatientIdOrderByTriggeredAtDesc(String patientId);

    List<AlertRecord> findByPatientIdAndSeverityOrderByTriggeredAtDesc(String patientId, AlertSeverity severity);
}