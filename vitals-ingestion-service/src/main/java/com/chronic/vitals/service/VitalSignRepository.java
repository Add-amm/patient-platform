package com.chronic.vitals.service;

import com.chronic.vitals.model.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {

    List<VitalSign> findByPatientIdOrderByMeasuredAtDesc(String patientId);
}