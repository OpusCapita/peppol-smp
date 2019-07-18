package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.*;
import com.opuscapita.peppol.smp.tickstar.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class TickstarScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TickstarScheduler.class);

    private TickstarClient client;
    private SmpRepository smpRepository;
    private EndpointService endpointService;
    private ParticipantService participantService;
    private DocumentTypeService documentTypeService;

    @Autowired
    public TickstarScheduler(TickstarClient client, SmpRepository smpRepository, EndpointService endpointService,
                             ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.client = client;
        this.smpRepository = smpRepository;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("TickstarScheduler started!");

        Smp smp = smpRepository.findByName(SmpName.TICKSTAR.name());
        updateDocumentTypes(smp);
        updateParticipants(smp);
    }

    private void updateDocumentTypes(Smp smp) {
        logger.info("TickstarScheduler updating document types...");
        TickstarMetadataListResponse response = client.getMetadataList();
        for (TickstarMetadataListProfile tickstarMetadata : response.getMetadataProfile()) {
            DocumentType documentType = documentTypeService.getDocumentType(tickstarMetadata.getProfileId(), smp);
            updateDocumentType(documentType, tickstarMetadata, smp);
        }
    }

    private void updateDocumentType(DocumentType documentType, TickstarMetadataListProfile tickstarMetadata, Smp smp) {
        if (documentType == null) {
            documentType = new DocumentType();
        }
        documentType.setDocumentTypeId(tickstarMetadata.getProfileId());
        documentType.setName(tickstarMetadata.getCommonName());
        documentType.setProfileIdentifier(tickstarMetadata.getProcessIdentifier().getValue());
        documentType.setDocumentIdentifier(tickstarMetadata.getDocumentIdentifier().getValue());
        documentType.setSmp(smp);

        documentTypeService.saveDocumentType(documentType);
    }

    private void updateParticipants(Smp smp) {
        logger.info("TickstarScheduler updating participants...");
        TickstarParticipantListResponse response = client.getAllParticipants();
        for (TickstarParticipantListParticipant tickstarParticipant : response.getParticipant()) {
            TickstarParticipantListParticipantIdentifier identifier = tickstarParticipant.getMeta().getParticipantIdentifier();
            Participant participant = participantService.getParticipant(identifier.getIdentifierCode(), identifier.getIdentifierValue());
            updateParticipant(participant, tickstarParticipant, smp);
        }
    }

    private void updateParticipant(Participant participant, TickstarParticipantListParticipant tickstarParticipant, Smp smp) {
        if (participant == null) {
            participant = new Participant();
        }
        participant.setName(tickstarParticipant.getBusinessCard().getBusinessEntity().get(0).getNames().getName().get(0).getName());
        participant.setIcd(tickstarParticipant.getMeta().getParticipantIdentifier().getIdentifierCode());
        participant.setIdentifier(tickstarParticipant.getMeta().getParticipantIdentifier().getIdentifierValue());
        participant.setCountry(tickstarParticipant.getBusinessCard().getBusinessEntity().get(0).getCountryCode());
        participant.setRegisteredAt(tickstarParticipant.getMeta().getRegistrationDate());

        Endpoint endpoint = endpointService.getEndpoint(smp);
        TickstarParticipantListAccessPointConfigurationMetadata metadata = getMetadataProfileIds(endpoint, tickstarParticipant);
        participant.setEndpoint(endpointService.getEndpoint(smp));
        participant.setDocumentTypes(getDocumentTypes(metadata, smp));

        participantService.saveParticipant(participant);
    }

    private Set<DocumentType> getDocumentTypes(TickstarParticipantListAccessPointConfigurationMetadata metadata, Smp smp) {
        Set<DocumentType> documentTypes = new HashSet<>();
        for (Integer id : metadata.getProfileId()) {
            documentTypes.add(documentTypeService.getDocumentType(id, smp));
        }
        return documentTypes;
    }

    private TickstarParticipantListAccessPointConfigurationMetadata getMetadataProfileIds(Endpoint endpoint, TickstarParticipantListParticipant tickstarParticipant) {
        List<TickstarParticipantListAccessPointConfiguration> apConfigs = tickstarParticipant.getAccessPointConfigurations().getAccessPointConfiguration();
        if (apConfigs == null) {
            return new TickstarParticipantListAccessPointConfigurationMetadata();
        }

        TickstarParticipantListAccessPointConfiguration apConfig = apConfigs.stream().filter(ap -> endpoint.getId().intValue() == ap.getEndpointId()).findFirst().orElse(null);
        if (apConfig == null) {
            return new TickstarParticipantListAccessPointConfigurationMetadata();
        }

        return apConfig.getMetadataProfileIds();
    }

}
