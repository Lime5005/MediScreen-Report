package com.lime.mediscreenreport.service;

import com.lime.mediscreenreport.config.FeignBadResponseWrapper;
import com.lime.mediscreenreport.model.Gender;
import com.lime.mediscreenreport.model.Patient;
import com.lime.mediscreenreport.model.Record;
import com.lime.mediscreenreport.proxy.PatientFeignProxy;
import com.lime.mediscreenreport.proxy.RecordFeignProxy;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class ReportService {

    private final PatientFeignProxy patientFeignProxy;
    private final RecordFeignProxy recordFeignProxy;

    public ReportService(PatientFeignProxy patientFeignProxy, RecordFeignProxy recordFeignProxy) {
        this.patientFeignProxy = patientFeignProxy;
        this.recordFeignProxy = recordFeignProxy;
    }

    public Patient getPatientInfo(Long patientId) {
        //This approach doesn't work, it will not return null, but an error.
//        ResponseEntity<Patient> patientById = patientFeignProxy.getPatientById(patientId);
//        if (patientById.getStatusCodeValue() == 400) {
//            return null;
//        }
//        return patientById.getBody();
        try {
            return patientFeignProxy.getPatientById(patientId).getBody();
        } catch (HystrixBadRequestException he) {
            if (he instanceof FeignBadResponseWrapper) {
                return null;
            } else {
                return null;
            }
        }
    }

    public List<Record> getPatientRecords(Long patientId) {
        try {
            return recordFeignProxy.getOnePatientRecords(patientId).getBody();
        } catch (HystrixBadRequestException he) {
            if (he instanceof FeignBadResponseWrapper) {
                return null;
            } else {
                return null;
            }
        }
    }

    public String calculateRiskByPatientId(Long patientId) {
        Patient patientInfo = getPatientInfo(patientId);
        List<Record> patientRecords = getPatientRecords(patientId);
        if (patientId == null || patientInfo == null || patientRecords == null) return null;

        Integer age = getPatientAge(patientInfo.getBirthDate());
        Gender sex = patientInfo.getSex();

        Integer terms = getTotalTriggerTerms(patientRecords);

        String assessment = "";

        if (terms == 0) {
            assessment = "aucun risque (None)";
        }

        if (age > 30) {
            if (terms >= 2 && terms < 6) {
                assessment = "risque limité (Borderline)";
            } else if (terms >= 6 && terms < 8) {
                assessment = "danger (In Danger)";
            } else if (terms >= 8) {
                assessment = "apparition précoce (Early onset)";
            }
        } else if (age < 30) {
            if (sex.equals(Gender.M)) {
                if (terms >= 3 && terms < 5) {
                    assessment = "danger (In Danger)";
                } else if (terms >= 5) {
                    assessment = "apparition précoce (Early onset)";
                }
            } else if (sex.equals(Gender.F)) {
                if (terms >= 4 && terms < 7) {
                    assessment = "danger (In Danger)";
                } else if (terms >= 7) {
                    assessment = "apparition précoce (Early onset)";
                }
            }
        }

        return assessment;
    }

    public Integer getPatientAge(Date birthDate) {
        int age = 0;
        if (birthDate != null) {
            Date today = new Date();//today = Thu Jan 27 19:08:25 CET 2022
            LocalDate localDateToday = convertToLocalDateViaSqlDate(today);
            LocalDate localBirthDate = convertToLocalDateViaSqlDate(birthDate);
            age = Period.between(localBirthDate, localDateToday).getYears();
        }
        return age;
    }

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }


    private Integer getTotalTriggerTerms(List<Record> patientRecords) {
        Integer result = 0;
        Set<String> terms = new HashSet<>();
        terms.add("Hémoglobine A1C");
        terms.add("Microalbumine");
        terms.add("Taille");
        terms.add("Poids");
        terms.add("Fume");
        terms.add("Anormal");
        terms.add("Cholestérol");
        terms.add("Vertige");
        terms.add("Rechute");
        terms.add("Réaction");
        terms.add("Anticorps");

        for (Record record : patientRecords) {
            for (String term : terms) {
                if (record.getNote().toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT))) {
                    result ++;
                }
            }
        }

        return result;
    }
}
