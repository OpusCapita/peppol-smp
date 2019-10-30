package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.tickstar.TickstarClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipant;
import no.difi.elma.smp.webservice.responses.AddParticipantResponse;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ParticipantServiceImpl implements ParticipantService {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    private final DifiClient difiClient;
    private final TickstarClient tickstarClient;
    private final ParticipantRepository repository;

    @Autowired
    public ParticipantServiceImpl(DifiClient difiClient, TickstarClient tickstarClient, ParticipantRepository repository) {
        this.repository = repository;
        this.difiClient = difiClient;
        this.tickstarClient = tickstarClient;
    }

    @Override
    public void saveParticipant(Participant participant) {
        repository.save(participant);
    }

    @Override
    public boolean saveParticipantRemote(Participant participant, Smp smp) {
        if (SmpName.DIFI.name().equals(smp.getName())) {
            ParticipantType participantType = new DifiParticipantBuilder()
                    .setName(participant.getName())
                    .setOrganizationNumber(participant.getIdentifier())
                    .setContactName(participant.getContactInfo())
                    .addAllProfiles(participant.getDocumentTypes().stream().map(DocumentType::getName).collect(Collectors.toList()))
                    .build();

            AddParticipantResponse response = difiClient.addParticipant(participantType);
            return response.getSuccess().isValue();
        }

        TickstarParticipant addRequest = TickstarParticipant.of(participant);
        tickstarClient.addParticipant(addRequest);
        return true;
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
    public Page<Participant> getAllParticipants(ParticipantRequestDto request) {
        Specification<Participant> spec = ParticipantFilterSpecification.filter(request.getFilter(), request.getPagination().getSorted());
        Pageable pageable = PageRequest.of(request.getPagination().getPage(), request.getPagination().getPageSize());
        return repository.findAll(spec, pageable);
    }
}
