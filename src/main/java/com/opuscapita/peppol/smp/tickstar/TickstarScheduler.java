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
import java.util.Set;

@Component
@EnableScheduling
public class TickstarScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TickstarScheduler.class);

    private TickstarClient client;
    private SmpRepository smpRepository;
    private EndpointRepository endpointRepository;
    private ParticipantService participantService;
    private DocumentTypeService documentTypeService;

    @Autowired
    public TickstarScheduler(TickstarClient client, SmpRepository smpRepository, EndpointRepository endpointRepository,
                             ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.client = client;
        this.smpRepository = smpRepository;
        this.endpointRepository = endpointRepository;
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
            DocumentType documentType = documentTypeService.getDocumentType(tickstarMetadata.getCommonName(), smp);
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

        TickstarParticipantListAccessPointConfiguration apConfiguration = tickstarParticipant.getAccessPointConfigurations().getAccessPointConfiguration().get(0);
        participant.setEndpoint(updateEndpoint(apConfiguration, smp));
        participant.setDocumentTypes(getDocumentTypes(apConfiguration, smp));

        participantService.saveParticipant(participant);
    }

    private Set<DocumentType> getDocumentTypes(TickstarParticipantListAccessPointConfiguration apConfiguration, Smp smp) {
        Set<DocumentType> documentTypes = new HashSet<>();
        for (Integer id : apConfiguration.getMetadataProfileIds().getProfileId()) {
            documentTypes.add(documentTypeService.getDocumentType(id, smp));
        }
        return documentTypes;
    }

    private Endpoint updateEndpoint(TickstarParticipantListAccessPointConfiguration apConfiguration, Smp smp) {
        logger.info("TickstarScheduler updating endpoints...");
        Endpoint endpoint = endpointRepository.findBySmp(smp).stream().filter(e -> e.getEndpointId() == apConfiguration.getEndpointId()).findFirst().orElse(null);
        if (endpoint == null) {
            endpoint = new Endpoint();
        }

        endpoint.setEndpointId(apConfiguration.getEndpointId());
        endpoint.setName(String.format("%s endpoint[%d]", smp.getName(), apConfiguration.getEndpointId()));
        endpoint.setSmp(smp);

        return endpointRepository.save(endpoint);
    }
}
