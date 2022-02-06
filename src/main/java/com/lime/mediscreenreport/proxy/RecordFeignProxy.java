package com.lime.mediscreenreport.proxy;

import com.lime.mediscreenreport.config.FeignPropagateBadRequestsConfiguration;
import com.lime.mediscreenreport.model.Record;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "mediscreen-record", url = "${record.serviceUrl}", configuration = FeignPropagateBadRequestsConfiguration.class)
public interface RecordFeignProxy {

    @GetMapping("/api/records/patient/{patientId}")
    ResponseEntity<List<Record>> getOnePatientRecords(@PathVariable(value = "patientId") Long patientId);
}
