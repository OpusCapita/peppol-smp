package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.*;
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

    @Value("${apis.tickstar.token:secret}")
    private String token;

    private final RestTemplate restTemplate;

    @Autowired
    public TickstarClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public TickstarParticipantListResponse getAllParticipants() {
        return exchange("/participant", HttpMethod.GET, TickstarParticipantListResponse.class);
    }

    public TickstarParticipantListParticipant getParticipant(String icd, String identifier) {
        String participantId = TickstarLookupParticipant.formParticipantId(icd, identifier);
        TickstarParticipantListResponse response = exchange("/participant?pid=" + participantId, HttpMethod.GET, TickstarParticipantListResponse.class);
        if (response == null || response.getParticipant() == null || response.getParticipant().isEmpty()) {
            return null;
        }
        return response.getParticipant().get(0);
    }

    public String addParticipant(TickstarParticipantAddRequest request) {
        HttpEntity<TickstarParticipantAddRequest> entity = new HttpEntity<>(request, getCommonHeaders());
        return exchange("/participant", HttpMethod.POST, entity, String.class);
    }

    public String editParticipant(TickstarParticipantAddRequest request) {
        HttpEntity<TickstarParticipantAddRequest> entity = new HttpEntity<>(request, getCommonHeaders());
        return exchange("/participant", HttpMethod.PUT, entity, String.class);
    }

    public String deleteParticipant(String icd, String identifier) {
        String participantId = TickstarLookupParticipant.formParticipantId(icd, identifier);
        return exchange("/participant?pid=" + participantId, HttpMethod.DELETE, String.class);
    }

    public TickstarMetadataListResponse getMetadataList() {
        return exchange("/metadataprofiles", HttpMethod.GET, TickstarMetadataListResponse.class);
    }

    private <T> T exchange(String url, HttpMethod method, Class<T> responseType) {
        HttpEntity<String> entity = new HttpEntity<>("", getCommonHeaders());
        return exchange(url, method, entity, responseType);
    }

    private <T> T exchange(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        String endpoint = String.format("https://api.galaxygw.com/2.0/smp%s", url);

        try {
            return restTemplate.exchange(endpoint, method, entity, responseType).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpHeaders getCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
