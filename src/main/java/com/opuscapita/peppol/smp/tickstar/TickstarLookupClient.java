package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TickstarLookupClient {

    @Value("${tickstar.token:secret}")
    private String token;

    private final RestTemplate restTemplate;

    public TickstarLookupClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public TickstarLookupResponse lookup(String icd, String identifier) {
        String participantId = TickstarLookupParticipant.formParticipantId(icd, identifier);
        String endpoint = "https://api.galaxygw.com/1.0/smp/smplookup.json?pid=" + participantId;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            return restTemplate.exchange(endpoint, HttpMethod.GET, entity, TickstarLookupResponse.class).getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
