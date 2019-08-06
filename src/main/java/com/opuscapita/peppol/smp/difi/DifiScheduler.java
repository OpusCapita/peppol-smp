package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.*;
import no.difi.elma.smp.webservice.responses.GetAllParticipantsResponse;
import no.difi.elma.smp.webservice.responses.GetParticipantResponse;
import no.difi.elma.smp.webservice.responses.ProfilesSupportedResponse;
import no.difi.elma.smp.webservice.types.CenbiiProfileType;
import no.difi.elma.smp.webservice.types.OrganizationNumberType;
import no.difi.elma.smp.webservice.types.ProfileType;
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
public class DifiScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DifiScheduler.class);

    private DifiClient client;
    private SmpRepository smpRepository;
    private EndpointService endpointService;
    private ParticipantService participantService;
    private DocumentTypeService documentTypeService;

    @Autowired
    public DifiScheduler(DifiClient client, SmpRepository smpRepository, EndpointService endpointService,
                         ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.client = client;
        this.smpRepository = smpRepository;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("DifiScheduler started!");

        Smp smp = smpRepository.findByName(SmpName.DIFI.name());
        updateDocumentTypes(smp);
        updateParticipants(smp);
    }

    private void updateDocumentTypes(Smp smp) {
        logger.info("DifiScheduler updating document types...");
        ProfilesSupportedResponse response = client.getSupportedProfiles();

        int id = 0;
        for (CenbiiProfileType difiMetadata : response.getCenbiiProfiles()) {
            DocumentType documentType = documentTypeService.getDocumentType(difiMetadata.getName().getValue(), smp);
            updateDocumentType(documentType, difiMetadata, smp, id++);
        }
    }

    private void updateDocumentType(DocumentType documentType, CenbiiProfileType difiMetadata, Smp smp, int id) {
        if (documentType == null) {
            documentType = new DocumentType();
        }
        documentType.setDocumentTypeId(id);
        documentType.setName(difiMetadata.getName().getValue());
        documentType.setSmp(smp);

        documentTypeService.saveDocumentType(documentType);
    }

    private void updateParticipants(Smp smp) {
        logger.info("DifiScheduler updating participants...");
        GetAllParticipantsResponse response = client.getAllParticipants();
        for (OrganizationNumberType difiParticipantIdentifier : response.getOrganizationNumber()) {
            GetParticipantResponse difiParticipant = client.getParticipant(difiParticipantIdentifier.getValue());
            Participant participant = participantService.getParticipant(DifiClient.ICD_9908, difiParticipantIdentifier.getValue());
            updateParticipant(participant, difiParticipant, smp);
        }
    }

    private void updateParticipant(Participant participant, GetParticipantResponse difiParticipant, Smp smp) {
        if (participant == null) {
            participant = new Participant();
        }
        participant.setName(difiParticipant.getParticipant().getOrganization().getName().getValue());
        participant.setCountry("NO");
        participant.setIcd(DifiClient.ICD_9908);
        participant.setIdentifier(difiParticipant.getParticipant().getOrganization().getOrganizationNumber().getValue());
        participant.setContactInfo(difiParticipant.getParticipant().getOrganization().getContact().getName().getValue());

        participant.setEndpoint(endpointService.getEndpoint(smp));
        participant.setDocumentTypes(getDocumentTypes(difiParticipant, smp));

        participantService.saveParticipant(participant);
    }

    private Set<DocumentType> getDocumentTypes(GetParticipantResponse difiParticipant, Smp smp) {
        Set<DocumentType> documentTypes = new HashSet<>();
        for (ProfileType profileType : difiParticipant.getParticipant().getProfiles()) {
            documentTypes.add(documentTypeService.getDocumentType(profileType.getValue(), smp));
        }
        return documentTypes;
    }

}
