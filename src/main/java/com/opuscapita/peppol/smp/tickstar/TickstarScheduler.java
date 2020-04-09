package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpName;
import com.opuscapita.peppol.smp.tickstar.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class TickstarScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TickstarScheduler.class);

    private TickstarClient client;
    private EndpointService endpointService;
    private ParticipantService participantService;
    private DocumentTypeService documentTypeService;

    @Autowired
    public TickstarScheduler(TickstarClient client, EndpointService endpointService, ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.client = client;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("TickstarScheduler started!");
        Endpoint endpoint = endpointService.getEndpoint(SmpName.TICKSTAR);

        updateDocumentTypes(endpoint);
        updateParticipants(endpoint);
    }

    private void updateDocumentTypes(Endpoint endpoint) {
        logger.info("...TickstarScheduler updating document types");

        TickstarMetadataListResponse response = client.getMetadataList();
        for (TickstarMetadataListProfile queriedDocumentType : response.getMetadataProfile()) {
            DocumentType persistedDocumentType = documentTypeService.getDocumentType(queriedDocumentType.getProfileId(), SmpName.TICKSTAR);
            convertDocumentType(persistedDocumentType, queriedDocumentType, endpoint);
        }
    }

    private void updateParticipants(Endpoint endpoint) {
        logger.info("...TickstarScheduler updating participants");

        TickstarParticipantListResponse response = client.getAllParticipants();
        for (TickstarParticipant queriedParticipant : response.getParticipant()) {
            TickstarParticipantIdentifier identifier = queriedParticipant.getMeta().getParticipantIdentifier();

            Participant persistedParticipant = participantService.getParticipant(identifier.getIdentifierCode(), identifier.getIdentifierValue());
            try {
                convertParticipant(persistedParticipant, queriedParticipant, endpoint);
            } catch (Exception e) {
                logger.error("Failed to convert the participant: " + identifier.getIdentifierValue(), e);
            }
        }
    }

    private void convertParticipant(Participant persistedParticipant, TickstarParticipant queriedParticipant, Endpoint endpoint) {
        if (persistedParticipant == null) {
            logger.warn("......TickstarScheduler found a new participant: " + queriedParticipant.getBusinessCard().getBusinessEntity().get(0).getNames().getName().get(0).getName() + ", saving");
            persistedParticipant = new Participant();
        }

        persistedParticipant.setName(queriedParticipant.getBusinessCard().getBusinessEntity().get(0).getNames().getName().get(0).getName());
        persistedParticipant.setIcd(queriedParticipant.getMeta().getParticipantIdentifier().getIdentifierCode());
        persistedParticipant.setIdentifier(queriedParticipant.getMeta().getParticipantIdentifier().getIdentifierValue());
        persistedParticipant.setCountry(queriedParticipant.getBusinessCard().getBusinessEntity().get(0).getCountryCode());
        persistedParticipant.setRegisteredAt(queriedParticipant.getMeta().getRegistrationDate());

        persistedParticipant.setEndpoint(endpoint);
        persistedParticipant.setDocumentTypes(convertDocumentTypeForParticipant(queriedParticipant, endpoint));

        participantService.saveParticipant(persistedParticipant, "system");
    }

    private Set<DocumentType> convertDocumentTypeForParticipant(TickstarParticipant queriedParticipant, Endpoint endpoint) {
        TickstarParticipantAccessPointConfigurationMetadata metadata = getTickstarMetadataProfileIds(queriedParticipant, endpoint);
        return metadata.getProfileId().stream()
                .map(profile -> documentTypeService.getDocumentType(profile, endpoint.getSmp()))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private TickstarParticipantAccessPointConfigurationMetadata getTickstarMetadataProfileIds(TickstarParticipant queriedParticipant, Endpoint endpoint) {
        List<TickstarParticipantAccessPointConfiguration> apConfigs = queriedParticipant.getAccessPointConfigurations().getAccessPointConfiguration();
        if (apConfigs == null) {
            return new TickstarParticipantAccessPointConfigurationMetadata();
        }

        return apConfigs.stream().filter(ap -> endpoint.getId().intValue() == ap.getEndpointId()).findFirst()
                .orElse(new TickstarParticipantAccessPointConfiguration()).getMetadataProfileIds();
    }

    private void convertDocumentType(DocumentType persistedDocumentType, TickstarMetadataListProfile queriedDocumentType, Endpoint endpoint) {
        if (persistedDocumentType != null) {
            return;
        }

        logger.warn("......TickstarScheduler found a new document type, saving");
        DocumentType newDocumentType = new DocumentType();
        newDocumentType.setExternalIdAsInteger(queriedDocumentType.getProfileId());
        newDocumentType.setName(queriedDocumentType.getCommonName());

        documentTypeService.saveDocumentType(newDocumentType, endpoint.getSmp());
    }
}
