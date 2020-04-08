package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpName;
import no.difi.elma.smp.webservice.responses.GetAllParticipantsResponse;
import no.difi.elma.smp.webservice.responses.GetParticipantResponse;
import no.difi.elma.smp.webservice.responses.ProfilesSupportedResponse;
import no.difi.elma.smp.webservice.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class DifiScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DifiScheduler.class);

    private DifiClient client;
    private EndpointService endpointService;
    private ParticipantService participantService;
    private DocumentTypeService documentTypeService;

    @Autowired
    public DifiScheduler(DifiClient client, EndpointService endpointService,
                         ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.client = client;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("DifiScheduler started!");
        Endpoint endpoint = endpointService.getEndpoint(SmpName.DIFI);

        updateDocumentTypes(endpoint);
        updateParticipants(endpoint);
    }

    private void updateDocumentTypes(Endpoint endpoint) {
        logger.info("...DifiScheduler updating document types");

        ProfilesSupportedResponse response = client.getSupportedProfiles();
        for (CenbiiProfileType queriedDocumentType : response.getCenbiiProfiles()) {
            DocumentType persistedDocumentType = documentTypeService.getDocumentType(queriedDocumentType.getName().getValue(), SmpName.DIFI);
            convertDocumentType(persistedDocumentType, queriedDocumentType, endpoint);
        }
    }

    private void updateParticipants(Endpoint endpoint) {
        logger.info("...DifiScheduler updating participants");

        GetAllParticipantsResponse response = client.getAllParticipants();
        for (OrganizationNumberType difiParticipantIdentifier : response.getOrganizationNumber()) {
            String identifier = difiParticipantIdentifier.getValue();
            for (String icd : DifiClient.getDifiIcd()) {
                GetParticipantResponse getResponse = client.getParticipant(icd + ":" + identifier);

                if (getResponse == null || getResponse.getParticipant() == null || getResponse.getParticipant().getOrganization() == null ||
                        getResponse.getParticipant().getOrganization().getName() == null) {
                    return;
                }

                Participant persistedParticipant = participantService.getParticipant(icd, identifier);
                try {
                    convertParticipant(icd, identifier, persistedParticipant, getResponse.getParticipant(), endpoint);
                } catch (Exception e) {
                    logger.error("Failed to convert the participant: " + icd + ":" + identifier, e);
                }
            }
        }
    }

    private void convertParticipant(String icd, String identifier, Participant persistedParticipant, ParticipantType queriedParticipant, Endpoint endpoint) {
        if (persistedParticipant == null) {
            logger.warn("......DifiScheduler found a new participant: " + icd + ":" + identifier + ", saving");
            persistedParticipant = new Participant();
        }

        OrganizationType organization = queriedParticipant.getOrganization();
        persistedParticipant.setName(organization.getName().getValue());
        persistedParticipant.setCountry("NO");
        persistedParticipant.setIcd(icd);
        persistedParticipant.setIdentifier(identifier);

        ContactType contact = organization.getContact();
        if (contact != null) {
            persistedParticipant.setContactName(contact.getName() != null ? contact.getName().getValue() : null);
            persistedParticipant.setContactEmail(contact.getEmail() != null ? contact.getEmail().getValue() : null);
            persistedParticipant.setContactPhone(contact.getTelephone() != null ? contact.getTelephone().getValue() : null);
        }

        persistedParticipant.setEndpoint(endpoint);
        persistedParticipant.setDocumentTypes(convertDocumentTypeForParticipant(queriedParticipant, endpoint));

        participantService.saveParticipant(persistedParticipant);
    }

    private Set<DocumentType> convertDocumentTypeForParticipant(ParticipantType queriedParticipant, Endpoint endpoint) {
        return queriedParticipant.getProfiles().stream()
                .map(profile -> documentTypeService.getDocumentType(profile.getValue(), endpoint.getSmp()))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void convertDocumentType(DocumentType persistedDocumentType, CenbiiProfileType queriedDocumentType, Endpoint endpoint) {
        if (persistedDocumentType != null) {
            return;
        }

        logger.warn("......DifiScheduler found a new document type: " + queriedDocumentType.getDescription().getValue() + ", saving");
        DocumentType newDocumentType = new DocumentType();
        newDocumentType.setExternalId(queriedDocumentType.getName().getValue());
        newDocumentType.setName(queriedDocumentType.getDescription().getValue());

        documentTypeService.saveDocumentType(newDocumentType, endpoint.getSmp());
    }
}
