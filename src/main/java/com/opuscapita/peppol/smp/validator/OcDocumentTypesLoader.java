package com.opuscapita.peppol.smp.validator;

import com.opuscapita.peppol.commons.auth.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class OcDocumentTypesLoader {

    private static final Logger logger = LoggerFactory.getLogger(OcDocumentTypesLoader.class);

    private final RestTemplate restTemplate;
    private final AuthorizationService authService;

    @Autowired
    public OcDocumentTypesLoader(AuthorizationService authService, RestTemplateBuilder restTemplateBuilder) {
        this.authService = authService;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<OcDocumentType> getOcDocumentTypes() {
        String endpoint = "http://peppol-validator:3039/api/get-document-types";
        logger.info("Sending get-document-types request to endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<List<OcDocumentType>> result = restTemplate.exchange(endpoint, HttpMethod.GET, entity, new ParameterizedTypeReference<List<OcDocumentType>>() {
            });
            logger.debug("Get-document-types request successfully sent, got response: " + result.toString());
            return result.getBody();

        } catch (Exception e) {
            logger.error("Error occurred while trying to send the get-document-types request", e);
            return new ArrayList<>();
        }
    }
}
