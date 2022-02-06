package com.lime.mediscreenreport.controller;

import com.lime.mediscreenreport.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ReportController {
    Logger logger = LoggerFactory.getLogger(ReportController.class);


    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/assessment/{patientId}")
    public ResponseEntity<String> getRiskAssessment(@PathVariable(value = "patientId") Long patientId) {
        String risk = reportService.calculateRiskByPatientId(patientId);
        if (risk == null) {
            logger.info("No data found with patientId: " + patientId);
            return new ResponseEntity<>("No data found with patientId: " + patientId, HttpStatus.BAD_REQUEST);
        }
        logger.info("Assessment data queried with patientId: " + patientId);
        return ResponseEntity.ok(risk);
    }
}
