package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.tickstar.TickstarClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantAccessPointConfiguration;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantAccessPointConfigurationMetadata;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import no.difi.elma.smp.webservice.responses.AddParticipantResponse;
import no.difi.elma.smp.webservice.responses.GetAllParticipantsResponse;
import no.difi.elma.smp.webservice.responses.GetParticipantResponse;
import no.difi.elma.smp.webservice.types.OrganizationNumberType;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParticipantServiceImpl implements ParticipantService {

    private final DifiClient difiClient;
    private final TickstarClient tickstarClient;

    private final EndpointService endpointService;
    private final DocumentTypeService documentTypeService;

    @Autowired
    public ParticipantServiceImpl(DifiClient difiClient, TickstarClient tickstarClient,
                                  EndpointService endpointService, DocumentTypeService documentTypeService) {
        this.difiClient = difiClient;
        this.tickstarClient = tickstarClient;

        this.endpointService = endpointService;
        this.documentTypeService = documentTypeService;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return getTickstarParticipants(getDifiParticipants(new ArrayList<>()));
    }

    private List<Participant> getDifiParticipants(List<Participant> participantList) {
        GetAllParticipantsResponse response = difiClient.getAllParticipants();
        for (OrganizationNumberType difiParticipantIdentifier : response.getOrganizationNumber()) {
            GetParticipantResponse difiParticipant = difiClient.getParticipant(difiParticipantIdentifier.getValue());
            participantList.add(convertToParticipant(difiParticipant));
        }

        return participantList;
    }

    private List<Participant> getTickstarParticipants(List<Participant> participantList) {
        TickstarParticipantListResponse response = tickstarClient.getAllParticipants();
        for (TickstarParticipant tickstarParticipant : response.getParticipant()) {
            participantList.add(convertToParticipant(tickstarParticipant));
        }

        return participantList;
    }

    private Participant convertToParticipant(GetParticipantResponse difiParticipant) {
        Participant participant = new Participant();

        participant.setName(difiParticipant.getParticipant().getOrganization().getName().getValue());
        participant.setCountry("NO");
        participant.setIcd(DifiClient.getDifiIcd());
        participant.setIdentifier(difiParticipant.getParticipant().getOrganization().getOrganizationNumber().getValue());
        participant.setContactInfo(difiParticipant.getParticipant().getOrganization().getContact().getName().getValue());

        participant.setEndpoint(endpointService.getEndpoint(SmpName.DIFI));
        participant.setDocumentTypes(convertToDocumentTypes(difiParticipant));

        return participant;
    }

    private Participant convertToParticipant(TickstarParticipant tickstarParticipant) {
        Participant participant = new Participant();

        participant.setName(tickstarParticipant.getBusinessCard().getBusinessEntity().get(0).getNames().getName().get(0).getName());
        participant.setIcd(tickstarParticipant.getMeta().getParticipantIdentifier().getIdentifierCode());
        participant.setIdentifier(tickstarParticipant.getMeta().getParticipantIdentifier().getIdentifierValue());
        participant.setCountry(tickstarParticipant.getBusinessCard().getBusinessEntity().get(0).getCountryCode());
        participant.setRegisteredAt(tickstarParticipant.getMeta().getRegistrationDate());

        participant.setEndpoint(endpointService.getEndpoint(SmpName.TICKSTAR));
        participant.setDocumentTypes(convertToDocumentTypes(tickstarParticipant, participant.getEndpoint()));

        return participant;
    }

    private Set<DocumentType> convertToDocumentTypes(GetParticipantResponse difiParticipant) {
        return difiParticipant.getParticipant().getProfiles().stream()
                .map(profile -> documentTypeService.getDocumentType(profile.getValue(), SmpName.DIFI))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private Set<DocumentType> convertToDocumentTypes(TickstarParticipant tickstarParticipant, Endpoint endpoint) {
        TickstarParticipantAccessPointConfigurationMetadata metadata = getTickstarMetadataProfileIds(endpoint, tickstarParticipant);
        return metadata.getProfileId().stream()
                .map(profile -> documentTypeService.getDocumentType(profile, SmpName.TICKSTAR))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private TickstarParticipantAccessPointConfigurationMetadata getTickstarMetadataProfileIds(Endpoint endpoint, TickstarParticipant tickstarParticipant) {
        List<TickstarParticipantAccessPointConfiguration> apConfigs = tickstarParticipant.getAccessPointConfigurations().getAccessPointConfiguration();
        if (apConfigs == null) {
            return new TickstarParticipantAccessPointConfigurationMetadata();
        }

        return apConfigs.stream().filter(ap -> endpoint.getId().intValue() == ap.getEndpointId()).findFirst()
                .orElse(new TickstarParticipantAccessPointConfiguration()).getMetadataProfileIds();
    }

    @Override
    public boolean saveParticipant(Participant participant) {
        return SmpName.DIFI.equals(participant.getEndpoint().getSmp().getName()) ?
                saveDifiParticipant(participant) : saveTickstarParticipant(participant);
    }

    private boolean saveDifiParticipant(Participant participant) {
        ParticipantType participantType = new DifiParticipantBuilder()
                .setName(participant.getName())
                .setOrganizationNumber(participant.getIdentifier())
                .setContactName(participant.getContactInfo())
                .addAllProfiles(participant.getDocumentTypes().stream().map(DocumentType::getExternalId).collect(Collectors.toList()))
                .build();

        AddParticipantResponse response = difiClient.addParticipant(participantType);
        return response.getSuccess().isValue();
    }

    private boolean saveTickstarParticipant(Participant participant) {
        TickstarParticipant addRequest = TickstarParticipant.of(participant);
        HttpStatus responseStatus = tickstarClient.addParticipant(addRequest);
        return responseStatus.is2xxSuccessful();
    }

}
