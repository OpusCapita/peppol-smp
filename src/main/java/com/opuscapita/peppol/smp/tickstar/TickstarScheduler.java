package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.helper.BusinessPlatformDefiner;
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
    private BusinessPlatformDefiner businessPlatformDefiner;

    @Autowired
    public TickstarScheduler(TickstarClient client, EndpointService endpointService,
                             ParticipantService participantService, DocumentTypeService documentTypeService,
                             BusinessPlatformDefiner businessPlatformDefiner) {
        this.client = client;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
        this.businessPlatformDefiner = businessPlatformDefiner;
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
            if (!checkEnvironment(queriedParticipant, endpoint)) {
                continue;
            }

            TickstarParticipantIdentifier identifier = queriedParticipant.getMeta().getParticipantIdentifier();
            Participant persistedParticipant = participantService.getParticipant(identifier.getIdentifierCode(), identifier.getIdentifierValue());
            try {
                convertParticipant(identifier.getIdentifierCode(), identifier.getIdentifierValue(), persistedParticipant, queriedParticipant, endpoint);
            } catch (Exception e) {
                logger.error("Failed to convert the participant: " + identifier.getIdentifierValue(), e);
            }
        }
    }

    private void convertParticipant(String icd, String identifier, Participant persistedParticipant, TickstarParticipant queriedParticipant, Endpoint endpoint) {
        if (persistedParticipant == null) {
            logger.warn("......TickstarScheduler found a new participant: " + icd + ":" + identifier + ", saving");
            persistedParticipant = new Participant();

        } else if (!isThereAnyUpdate(persistedParticipant, queriedParticipant, endpoint)) {
            logger.debug("......TickstarScheduler found participant: " + icd + ":" + identifier + ", ignoring with no-change");
            return;

        } else {
            logger.info("......TickstarScheduler found an update for participant: " + icd + ":" + identifier + ", saving");
        }

        TickstarParticipantBusinessCard businessCard = queriedParticipant.getBusinessCard() != null ? queriedParticipant.getBusinessCard() : new TickstarParticipantBusinessCard();
        TickstarParticipantBusinessEntity businessEntity = businessCard.getBusinessEntity() != null && !businessCard.getBusinessEntity().isEmpty() ? businessCard.getBusinessEntity().get(0) : new TickstarParticipantBusinessEntity();
        TickstarParticipantContact businessContact = businessEntity.getContacts() != null && businessEntity.getContacts().getContact() != null && !businessEntity.getContacts().getContact().isEmpty() ? businessEntity.getContacts().getContact().get(0) : new TickstarParticipantContact();

        persistedParticipant.setIcd(icd);
        persistedParticipant.setIdentifier(identifier);
        persistedParticipant.setName(businessEntity.getNames().getName().get(0).getName());
        persistedParticipant.setCountry(businessEntity.getCountryCode());
        persistedParticipant.setRegisteredAt(queriedParticipant.getMeta().getRegistrationDate());
        persistedParticipant.setContactName(businessContact.getName());
        persistedParticipant.setContactEmail(businessContact.getEmail());
        persistedParticipant.setContactPhone(businessContact.getPhoneNumber());

        persistedParticipant.setEndpoint(endpoint);
        persistedParticipant.setBusinessPlatform(businessPlatformDefiner.define(persistedParticipant));
        persistedParticipant.setDocumentTypes(convertDocumentTypeForParticipant(queriedParticipant, endpoint));

        participantService.saveParticipant(persistedParticipant, "System");
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

    // participant has any document-type registered in this environment
    private boolean checkEnvironment(TickstarParticipant queriedParticipant, Endpoint endpoint) {
        TickstarParticipantAccessPointConfigurationMetadata metadata = getTickstarMetadataProfileIds(queriedParticipant, endpoint);
        return metadata.getProfileId() != null && !metadata.getProfileId().isEmpty();
    }

    private boolean isThereAnyUpdate(Participant persistedParticipant, TickstarParticipant queriedParticipant, Endpoint endpoint) {
        TickstarParticipantBusinessCard businessCard = queriedParticipant.getBusinessCard() != null ? queriedParticipant.getBusinessCard() : new TickstarParticipantBusinessCard();
        TickstarParticipantBusinessEntity businessEntity = businessCard.getBusinessEntity() != null && !businessCard.getBusinessEntity().isEmpty() ? businessCard.getBusinessEntity().get(0) : new TickstarParticipantBusinessEntity();
        if (!persistedParticipant.getName().equals(businessEntity.getNames().getName().get(0).getName())) {
            return true;
        }
        if (!persistedParticipant.getCountry().equals(businessEntity.getCountryCode())) {
            return true;
        }

        TickstarParticipantContact businessContact = businessEntity.getContacts() != null && businessEntity.getContacts().getContact() != null && !businessEntity.getContacts().getContact().isEmpty() ? businessEntity.getContacts().getContact().get(0) : new TickstarParticipantContact();
        if (businessContact.getName() != null && !businessContact.getName().equals(persistedParticipant.getContactName())) {
            return true;
        }
        if (businessContact.getEmail() != null && !businessContact.getEmail().equals(persistedParticipant.getContactEmail())) {
            return true;
        }
        if (businessContact.getPhoneNumber() != null && !businessContact.getPhoneNumber().equals(persistedParticipant.getContactPhone())) {
            return true;
        }

        TickstarParticipantAccessPointConfigurationMetadata metadata = getTickstarMetadataProfileIds(queriedParticipant, endpoint);
        if (metadata.getProfileId().size() != persistedParticipant.getDocumentTypes().size()) {
            return true;
        }
        for (Integer profileId : metadata.getProfileId()) {
            if (persistedParticipant.getDocumentTypes().stream().noneMatch(d -> d.getExternalId().equals(String.valueOf(profileId)))) {
                return true;
            }
        }

        // if we have a participant without businessPlatform, we fix it using scheduler
        if (persistedParticipant.getBusinessPlatform() == null) {
            return true;
        }

        return false;
    }
}
