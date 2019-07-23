package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.data.domain.Page;

public interface ParticipantService {

    void saveParticipant(Participant participant);

    Participant getParticipant(Long id);

    Participant getParticipant(String icdIdentifier);

    Participant getParticipant(String icd, String identifier);

    Page<Participant> getAllParticipants(ParticipantRequestDto request);

}
