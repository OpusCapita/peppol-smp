package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParticipantService {

    Participant getParticipant(Long id);

    Participant getParticipant(String icdIdentifier);

    Participant getParticipant(String icd, String identifier);

    Page<Participant> getAllParticipants(ParticipantRequestDto request);

    void saveParticipant(Participant participant);

    boolean saveParticipantRemote(Participant participant);

    void deleteParticipant(Participant participant);

    boolean deleteParticipantRemote(Participant participant);
}
