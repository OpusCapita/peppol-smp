package com.opuscapita.peppol.smp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SmpApp {

    public static void main(String[] args) {
        SpringApplication.run(SmpApp.class, args);
    }
}
