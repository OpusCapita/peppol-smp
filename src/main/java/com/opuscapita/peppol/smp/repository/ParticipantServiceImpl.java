package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import com.opuscapita.peppol.smp.entity.*;
import com.opuscapita.peppol.smp.tickstar.TickstarClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipant;
import no.difi.elma.smp.webservice.responses.AddParticipantResponse;
import no.difi.elma.smp.webservice.responses.DeleteParticipantResponse;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ParticipantServiceImpl implements ParticipantService {

    private final DifiClient difiClient;
    private final TickstarClient tickstarClient;
    private final ParticipantRepository repository;
    private final OperationHistoryService operationHistoryService;

    @Autowired
    public ParticipantServiceImpl(DifiClient difiClient, TickstarClient tickstarClient,
                                  ParticipantRepository repository, OperationHistoryService operationHistoryService) {
        this.difiClient = difiClient;
        this.tickstarClient = tickstarClient;
        this.repository = repository;
        this.operationHistoryService = operationHistoryService;
    }

    @Override
    public Participant getParticipant(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Participant getParticipant(String icdIdentifier) {
        if (StringUtils.isBlank(icdIdentifier)) {
            return null;
        }

        String[] parts = icdIdentifier.split(":");
        if (parts.length != 2) {
            return null;
        }

        return getParticipant(parts[0], parts[1]);
    }

    @Override
    public Participant getParticipant(String icd, String identifier) {
        return repository.findByIdentifier(identifier).stream().filter(p -> icd.equals(p.getIcd())).findFirst().orElse(null);
    }

    @Override
    public BusinessPlatform getBusinessPlatform(String icd, String identifier) {
        BusinessParticipant participant = repository.findByIcdAndIdentifier(icd, identifier);
        return participant.getBusinessPlatform();
    }

    @Override
    public Page<Participant> getAllParticipants(ParticipantRequestDto request) {
        Specification<Participant> spec = ParticipantFilterSpecification.filter(request.getFilter(), request.getPagination().getSorted());
        Pageable pageable = PageRequest.of(request.getPagination().getPage(), request.getPagination().getPageSize());
        return repository.findAll(spec, pageable);
    }

    @Override
    public void saveParticipant(Participant participant, String userId) {
        operationHistoryService.saveLog(participant, userId);
        repository.save(participant);
    }

    @Override
    public void deleteParticipant(Participant participant, String userId) {
        operationHistoryService.saveLog(participant, userId, OperationType.DELETE);
        repository.delete(participant);
    }

    @Override
    public boolean saveParticipantRemote(Participant participant) {
        if (SmpName.DIFI.equals(participant.getEndpoint().getSmp().getName())) {
            return saveDifiParticipant(participant);
        }
        return saveTickstarParticipant(participant);
    }

    private boolean saveDifiParticipant(Participant participant) {
        ParticipantType participantType = new DifiParticipantBuilder()
                .setName(participant.getName())
                .setOrganizationNumber(participant.getIcdIdentifier())
                .setContactName(participant.getContactName())
                .setContactEmail(participant.getContactEmail())
                .setContactTelephone(participant.getContactPhone())
                .setWebsite("http://www.opuscapita.com")
                .addAllProfiles(participant.getDocumentTypes().stream().map(DocumentType::getExternalId).collect(Collectors.toList()))
                .build();

        if (participant.getId() != null) {
            return difiClient.editParticipant(participant.getIcdIdentifier(), participantType).getSuccess().isValue();
        }
        return difiClient.addParticipant(participantType).getSuccess().isValue();
    }

    private boolean saveTickstarParticipant(Participant participant) {
        TickstarParticipant addRequest = TickstarParticipant.of(participant);
        HttpStatus responseStatus = participant.getId() == null ? tickstarClient.addParticipant(addRequest) : tickstarClient.editParticipant(addRequest);
        if (responseStatus.equals(HttpStatus.CONFLICT)) {
            responseStatus = tickstarClient.editParticipant(addRequest);
        }
        return responseStatus.is2xxSuccessful();
    }

    @Override
    public boolean deleteParticipantRemote(Participant participant) {
        return SmpName.DIFI.equals(participant.getEndpoint().getSmp().getName()) ?
                deleteDifiParticipant(participant) : deleteTickstarParticipant(participant);
    }

    private boolean deleteDifiParticipant(Participant participant) {
        DeleteParticipantResponse response = difiClient.deleteParticipant(participant.getIcdIdentifier());
        return response.getSuccess().isValue();
    }

    private boolean deleteTickstarParticipant(Participant participant) {
        HttpStatus responseStatus = tickstarClient.deleteParticipant(participant.getIcd(), participant.getIdentifier());
        return responseStatus.is2xxSuccessful();
    }

}
