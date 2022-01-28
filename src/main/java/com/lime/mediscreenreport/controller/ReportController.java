package com.lime.mediscreenreport.controller;

import com.lime.mediscreenreport.proxy.PatientFeignProxy;
import com.lime.mediscreenreport.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/assessment/{patientId}")
    public ResponseEntity<String> getRiskAssessment(@PathVariable(value = "patientId") Long patientId) {
        String risk = reportService.calculateRiskByPatientId(patientId);
        if (risk == null) {
            return new ResponseEntity<>("No data", HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(risk);
    }
}
