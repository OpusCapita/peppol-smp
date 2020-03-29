package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarMetadataListResponse;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
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

    public TickstarParticipant getParticipant(String icd, String identifier) {
        String participantId = TickstarLookupParticipant.formParticipantId(icd, identifier);
        TickstarParticipantListResponse response = exchange("/participant?pid=" + participantId, HttpMethod.GET, TickstarParticipantListResponse.class);
        if (response == null || response.getParticipant() == null || response.getParticipant().isEmpty()) {
            return null;
        }
        return response.getParticipant().get(0);
    }

    public HttpStatus addParticipant(TickstarParticipant request) {
        HttpEntity<TickstarParticipant> entity = new HttpEntity<>(request, getCommonHeaders());
        return restTemplate.exchange(getApiUrl("/participant"), HttpMethod.POST, entity, String.class).getStatusCode();
    }

    public String editParticipant(TickstarParticipant request) {
        HttpEntity<TickstarParticipant> entity = new HttpEntity<>(request, getCommonHeaders());
        return exchange("/participant", HttpMethod.PUT, entity, String.class);
    }

    public HttpStatus deleteParticipant(String icd, String identifier) {
        String participantId = TickstarLookupParticipant.formParticipantId(icd, identifier);
        HttpEntity<String> entity = new HttpEntity<>("", getCommonHeaders());
        return restTemplate.exchange(getApiUrl("/participant?pid=" + participantId), HttpMethod.DELETE, entity, String.class).getStatusCode();
    }

    public TickstarMetadataListResponse getMetadataList() {
        return exchange("/metadataprofile", HttpMethod.GET, TickstarMetadataListResponse.class);
    }

    public String getEndpointList() {
        return exchange("/endpoint", HttpMethod.GET, String.class);
    }

    private <T> T exchange(String url, HttpMethod method, Class<T> responseType) {
        HttpEntity<String> entity = new HttpEntity<>("", getCommonHeaders());
        return exchange(url, method, entity, responseType);
    }

    private <T> T exchange(String uri, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        return restTemplate.exchange(getApiUrl(uri), method, entity, responseType).getBody();
    }

    private String getApiUrl(String uri) {
        return String.format("https://api.galaxygw.com/2.0/smp%s", uri);
    }

    private HttpHeaders getCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
