package com.lime.mediscreenreport.service;

import com.lime.mediscreenreport.model.Patient;
import com.lime.mediscreenreport.model.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    public void testGetPatientAge() {
        Patient patientInfo = reportService.getPatientInfo(26L);
        Date birthDate = patientInfo.getBirthDate();
        System.out.println("birthDate = " + birthDate);
        Integer patientAge = reportService.getPatientAge(birthDate);
        System.out.println("patientAge = " + patientAge);
        assertTrue(patientAge == 69);
    }

    @Test
    public void testGetPatientAssessment() {
        List<Record> records = reportService.getPatientRecords(26L);
        for (Record record : records) {
            System.out.println("record = " + record.getNote());
        }
        String risk = reportService.calculateRiskByPatientId(26L);
        System.out.println("risk = " + risk);
        assertEquals("danger (In Danger)", risk);
    }

}
