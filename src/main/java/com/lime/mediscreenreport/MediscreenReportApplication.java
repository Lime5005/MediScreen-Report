package com.lime.mediscreenreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@EnableFeignClients
public class MediscreenReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediscreenReportApplication.class, args);
    }

}
