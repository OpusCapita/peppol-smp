package com.opuscapita.peppol.smp.difi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class DifiConfiguration {

    @Value("${difi.username:me}")
    private String username;

    @Value("${difi.password:em}")
    private String password;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("no.difi.elma.smp.webservice");
        return marshaller;
    }

    @Bean
    public DifiClient difiClient(Jaxb2Marshaller marshaller) {
        DifiClient client = new DifiClient(username, password);
        client.setDefaultUri("https://smp.difi.no/ws/2.0?wsdl");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
