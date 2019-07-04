package com.opuscapita.peppol.smp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({"com.opuscapita.peppol.smp", "com.opuscapita.peppol.commons"})
public class SmpApp {

    public static void main(String[] args) {
        SpringApplication.run(SmpApp.class, args);
    }
}
