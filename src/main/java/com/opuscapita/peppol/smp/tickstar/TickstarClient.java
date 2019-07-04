package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TickstarClient {

    @Value("${tickstar.token:secret}")
    private String token;

    private final RestTemplate restTemplate;

    @Autowired
    public TickstarClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public TickstarParticipantListResponse getAllParticipants() throws Exception {
        return exchange("/participant", HttpMethod.GET, TickstarParticipantListResponse.class);
    }

    private <T> T exchange(String url, HttpMethod method, Class<T> responseType) throws Exception {
        String endpoint = String.format("https://api.galaxygw.com/2.0/smp%s", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            return restTemplate.exchange(endpoint, method, entity, responseType).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
